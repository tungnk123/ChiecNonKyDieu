package com.uit.chiecnonkydieu.ui.wheelSpinnerViewModel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uit.chiechnonkydieu.R
import rubikstudio.library.model.LuckyItem
import java.util.*

class WheelSpinnerViewModel ( itemList: List<String>, context: Context ): ViewModel() {
    private val luckyItemList = MutableLiveData<List<LuckyItem>>()
    val liveLuckyItemList: LiveData<List<LuckyItem>>
        get() = luckyItemList
    private val spinIndex = MutableLiveData<Int>()
    val liveSpinIndex : LiveData<Int>
        get() = spinIndex
    private val colors = arrayOf(R.color.orange, R.color.black)

    init {
        val listItems = mutableListOf<LuckyItem>()

        for (i in itemList.indices) run {
            val luckyItem = LuckyItem()
            luckyItem.secondaryText = itemList[i]
            luckyItem.topText = ( i + 1 ).toString()
            luckyItem.color = context.getColor( this.colors[if (i % 2 == 0) 0 else 1] )
            listItems.add(luckyItem)
        }

        this.luckyItemList.value = listItems
    }

    fun spinWheel() {
        val rand = Random()
        spinIndex.value = rand.nextInt(this.luckyItemList.value!!.size - 1) + 0
    }

    fun doneSpinning(){
        spinIndex.value = -1
    }

}

