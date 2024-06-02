package com.uit.chiecnonkydieu.ui.Payment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.uit.chiecnonkydieu.adapter.MyAdapter
import com.uit.chiecnonkydieu.callback.OnItemClickListener
import com.uit.chiecnonkydieu.data.CoinPayment
import com.uit.paymentapi.util.PaymentsUtil
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.samples.pay.viewmodel.CheckoutViewModel
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityPaymentBinding
import java.text.DecimalFormat

class PaymentActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var coinPaymentList: ArrayList<CoinPayment>
    private lateinit var googlePayButton: PayButton
    private lateinit var money: String;
    private lateinit var coinReceived: String;
    private lateinit var currentCoin: String;
    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            // Task completed successfully
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    model.setPaymentData(it)
                    val currentCoin = coinReceived.toInt() + binding.tvCurrentCoin.text.toString().replace(".", "") .toInt();
                    val formatter = DecimalFormat("#,###")
                    val formattedCurrentCoin = formatter.format(currentCoin)
                    binding.tvCurrentCoin.text = formattedCurrentCoin.replace(",", ".");
                    Toast.makeText(this, "You have input $money", Toast.LENGTH_SHORT).show()
                }
            }
            //CommonStatusCodes.CANCELED -> The user canceled
            //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
            //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
        }
    }

    private val model: CheckoutViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize coinPaymentList
        coinPaymentList = ArrayList()
        currentCoin = binding.tvCurrentCoin.text.toString()
        // Use GridLayoutManager with 2 columns
        val layoutManager = GridLayoutManager(this, 2)
        binding.recycleview.layoutManager = layoutManager
        binding.recycleview.setHasFixedSize(true)


        _init()
        // Bind and initialize your Google Pay button
        googlePayButton = binding.googlePayPaymentButton;
        googlePayButton.initialize(
            ButtonOptions.newBuilder()
            .setAllowedPaymentMethods(PaymentsUtil.allowedPaymentMethods.toString())
            .build())

        // Add a click listener to your button to start the payment process
        googlePayButton.setOnClickListener({
            requestPayment()
        })

        binding.backButton.setOnClickListener{
            finish()
        }

//        model.canUseGooglePay.observe(this, Observer(::setGooglePayAvailable))
//        // Check whether Google Pay is available and show or hide your Google Pay
//        // button depending on the result

    }

    private fun _init() {
        coinPaymentList.add(CoinPayment("100", "0", R.drawable._20, "20.000Đ"))
        coinPaymentList.add(CoinPayment("250", "0", R.drawable._50, "50.000Đ"))
        coinPaymentList.add(CoinPayment("550", "+10%", R.drawable._100, "100.000Đ"))
        coinPaymentList.add(CoinPayment("1.200", "+20%", R.drawable._200, "200.000Đ"))
        coinPaymentList.add(CoinPayment("7.500", "+50%", R.drawable._500, "500.000Đ"))
        coinPaymentList.add(CoinPayment("20.000", "+100%", R.drawable._1000, "1.000.000Đ"))

        val adapter = MyAdapter(coinPaymentList, this)
        binding.recycleview.adapter = adapter
//        binding.googlePayPaymentButton.setOnClickListener({
//            requestPayment()
//        })

    }
    override fun onItemClick(position: Int) {
        val selectedItem = coinPaymentList[position]
        Toast.makeText(this, "Selected: ${selectedItem.amountCoin} coins", Toast.LENGTH_SHORT).show()
        // Handle the selected item here
        money = selectedItem.amountCoin
        coinReceived = selectedItem.amountCoin.replace(".", "")
    }
    private fun requestPayment() {
        if (money == null) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            return
        } else {
            val task = model.getLoadPaymentDataTask(priceCents = 1000L)
            task.addOnCompleteListener(paymentDataLauncher::launch)
        }
        // Disable the button to prevent multiple clicks
        // googlePayButton.isEnabled = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.

//        val task = model.getLoadPaymentDataTask(priceCents = 1000L);
//        task.addOnCompleteListener {
//            completedTask ->
//            if (completedTask.isSuccessful) {
//                completedTask.result.let(::handlePaymentSuccess)
//            }
//        }

    }
}