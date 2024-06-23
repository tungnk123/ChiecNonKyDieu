package com.uit.chiecnonkydieu.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uit.chiecnonkydieu.AppContainer
import com.uit.chiecnonkydieu.model.GemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel: ViewModel() {
    private val container = AppContainer()
    var gemResponse: GemResponse? = null
    fun getItems(username: String): GemResponse? {
        viewModelScope.launch(Dispatchers.IO) {
            gemResponse = container.gemApi.getItems(username)
        }
        return gemResponse
    }
}