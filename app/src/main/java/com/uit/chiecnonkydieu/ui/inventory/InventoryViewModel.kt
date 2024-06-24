package com.uit.chiecnonkydieu.ui.inventory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.chiecnonkydieu.AppContainer
import com.uit.chiecnonkydieu.model.GemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryViewModel : ViewModel() {
    private val container = AppContainer()
    private val _gemResponse = MutableLiveData<GemResponse?>()
    val gemResponse: LiveData<GemResponse?> get() = _gemResponse

    fun getItems(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = container.gemApi.getItems(username)
            call.enqueue(object : Callback<GemResponse> {
                override fun onResponse(call: Call<GemResponse>, response: Response<GemResponse>) {
                    if (response.isSuccessful) {
                        _gemResponse.postValue(response.body())
                    } else {
                        _gemResponse.postValue(null)
                    }
                }

                override fun onFailure(call: Call<GemResponse>, t: Throwable) {
                    _gemResponse.postValue(null)
                }
            })
        }
    }

    fun sellItems(username: String, password: String, itemId: Int, quantity: Int, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = container.gemApi.sellItems(username, password, itemId, quantity, price)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Handle successful sell response, e.g., update UI or notify user
                        Log.d("SellItem", "Item sold successfully")
                    } else {
                        // Handle failure, e.g., log error or notify user
                        Log.e("SellItem", "Sell item failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle call failure, e.g., log error or notify user
                    Log.e("SellItem", "Sell item call failed: ${t.message}", t)
                }
            })
        }
    }
}
