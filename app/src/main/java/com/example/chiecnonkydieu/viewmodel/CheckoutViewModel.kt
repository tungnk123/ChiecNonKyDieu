/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.pay.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.paymentapi.util.PaymentsUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executor
import kotlin.coroutines.resume

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val _paymentUiState: MutableStateFlow<PaymentUiState> =
        MutableStateFlow(PaymentUiState.NotStarted)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    // A client for interacting with the Google Pay API.
    private val paymentsClient: PaymentsClient = PaymentsUtil.createPaymentsClient(application)

    init {
        viewModelScope.launch {
            verifyGooglePayReadiness()
        }
    }

    /**
     * Determine the user's ability to pay with a payment method supported by your app and display
     * a Google Pay payment button.
    ) */
    private suspend fun verifyGooglePayReadiness() {
        val newUiState: PaymentUiState = try {
            if (fetchCanUseGooglePay()) {
                PaymentUiState.Available
            } else {
                PaymentUiState.Error(CommonStatusCodes.ERROR)
            }
        } catch (exception: ApiException) {
            PaymentUiState.Error(exception.statusCode, exception.message)
        }

        _paymentUiState.update { newUiState }
    }

    /**
     * Determine the user's ability to pay with a payment method supported by your app.
    ) */
    private suspend fun fetchCanUseGooglePay(): Boolean {
        val request = IsReadyToPayRequest.fromJson(PaymentsUtil.isReadyToPayRequest().toString())
        return paymentsClient.isReadyToPay(request).await()
    }

    /**
     * Creates a [Task] that starts the payment process with the transaction details included.
     *
     * @return a [Task] with the payment information.
     * @see [PaymentDataRequest](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient#loadPaymentData(com.google.android.gms.wallet.PaymentDataRequest)
    ) */
    fun getLoadPaymentDataTask(priceCents: Long): Task<PaymentData> {
        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        return paymentsClient.loadPaymentData(request)
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int, message: String?) {
        Log.e("Google Pay API error", "Error code: $statusCode, Message: $message")
    }

    fun setPaymentData(paymentData: PaymentData) {
        val payState = extractPaymentBillingName(paymentData)?.let {
            PaymentUiState.PaymentCompleted(payerName = it)
        } ?: PaymentUiState.Error(CommonStatusCodes.INTERNAL_ERROR)

        _paymentUiState.update { payState }
    }

    private fun extractPaymentBillingName(paymentData: PaymentData): String? {
        val paymentInformation = paymentData.toJson()

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            // Logging token string.
            Log.d(
                "Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
            )

            return billingName
        } catch (error: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $error")
        }

        return null
    }
}

abstract class PaymentUiState internal constructor() {
    object NotStarted : PaymentUiState()
    object Available : PaymentUiState()
    class PaymentCompleted(val payerName: String) : PaymentUiState()
    class Error(val code: Int, val message: String? = null) : PaymentUiState()
}

suspend fun <T> Task<T>.awaitTask(cancellationTokenSource: CancellationTokenSource? = null): Task<T> {
    return if (isComplete) this else suspendCancellableCoroutine { cont ->
        // Run the callback directly to avoid unnecessarily scheduling on the main thread.
        addOnCompleteListener(DirectExecutor, cont::resume)

        cancellationTokenSource?.let { cancellationSource ->
            cont.invokeOnCancellation { cancellationSource.cancel() }
        }
    }
}

/**
 * An [Executor] that just directly executes the [Runnable].
 */
private object DirectExecutor : Executor {
    override fun execute(r: Runnable) {
        r.run()
    }
}
