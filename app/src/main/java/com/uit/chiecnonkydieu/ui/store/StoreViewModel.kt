package com.uit.chiecnonkydieu.ui.store

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.chiecnonkydieu.AppContainer
import com.uit.chiecnonkydieu.model.ItemMarketDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel : ViewModel() {

    val appContainer = AppContainer()
    private val _itemMarketDto = MutableLiveData<List<ItemMarketDto>>()
    val itemMarketDto: LiveData<List<ItemMarketDto>>
        get() = _itemMarketDto

    fun getStoreItems() {
        viewModelScope.launch(Dispatchers.IO) {
            appContainer.gemApi.getAllListings().enqueue(object : Callback<List<ItemMarketDto>> {
                override fun onResponse(
                    call: Call<List<ItemMarketDto>>,
                    response: Response<List<ItemMarketDto>>
                ) {
                    if (response.isSuccessful) {
                        _itemMarketDto.postValue(response.body())
                    } else {
                        Log.e("StoreViewModel", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemMarketDto>>, t: Throwable) {
                    Log.e("StoreViewModel", "Failure: ${t.message}", t)
                }
            })
        }
    }
}
