package com.example.chiecnonkydieu.ui.wheel

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.LetterCard
import rubikstudio.library.model.LuckyItem
class WheelViewModel: ViewModel() {
    private val _luckyItemsList: MutableList<LuckyItem> = mutableListOf()
    val luckyItemsList:List<LuckyItem> = _luckyItemsList

    private val _letterCardList = MutableLiveData<MutableList<LetterCard>>()
    val letterCardList: LiveData<MutableList<LetterCard>> = _letterCardList
    fun initLuckyItemList(context: Context) {
        val luckyItem: LuckyItem = LuckyItem()
        luckyItem.secondaryText = "100"
        luckyItem.color = ContextCompat.getColor(context, R.color.purple_300)
        _luckyItemsList.add(luckyItem)

        val luckyItem2: LuckyItem = LuckyItem()
        luckyItem2.secondaryText = "400"
        luckyItem2.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem2)

        val luckyItem3: LuckyItem = LuckyItem()
        luckyItem3.secondaryText = "800"
        luckyItem3.color = ContextCompat.getColor(context, R.color.blue_200)
        _luckyItemsList.add(luckyItem3)

        val luckyItem4: LuckyItem = LuckyItem()
        luckyItem4.topText = "EXTRA TURN"
        luckyItem4.icon = R.drawable.ic_man
        luckyItem4.color = ContextCompat.getColor(context, R.color.purple_500)
        _luckyItemsList.add(luckyItem4)

        val luckyItem5: LuckyItem = LuckyItem()
        luckyItem5.secondaryText = "1200"
        luckyItem5.color = ContextCompat.getColor(context, R.color.gray)
        _luckyItemsList.add(luckyItem5)

        val luckyItem6: LuckyItem = LuckyItem()
        luckyItem6.secondaryText = "600"
        luckyItem6.color = ContextCompat.getColor(context, R.color.blue_200)

        _luckyItemsList.add(luckyItem6)

        val luckyItem7: LuckyItem = LuckyItem()
        luckyItem7.topText = "MISS TURN"
        luckyItem7.icon = R.drawable.ic_man
        luckyItem7.color = ContextCompat.getColor(context, R.color.purple_500)
        _luckyItemsList.add(luckyItem7)

        val luckyItem8: LuckyItem = LuckyItem()
        luckyItem8.secondaryText = "800"
        luckyItem8.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem8)

        val luckyItem9: LuckyItem = LuckyItem()
        luckyItem9.secondaryText = "1500"
        luckyItem9.color = ContextCompat.getColor(context, R.color.purple_300)
        _luckyItemsList.add(luckyItem9)

        val luckyItem10: LuckyItem = LuckyItem()
        luckyItem10.secondaryText = "400"
        luckyItem10.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem10)

        val luckyItem11: LuckyItem = LuckyItem()
        luckyItem11.topText = "ITEM"
        luckyItem11.icon = R.drawable.ic_wheel
        luckyItem11.color = ContextCompat.getColor(context, R.color.orange)
        _luckyItemsList.add(luckyItem11)
    }

    fun getIndexAfterRotate(): Int {
        // TODO
        val size = _luckyItemsList.size - 1
        return (0 .. size).random()
    }
    fun getStringItemAtIndex(index: Int): String {
        return if (!_luckyItemsList[index].secondaryText.isNullOrEmpty()) {
            _luckyItemsList[index].secondaryText
        }
        else {
            _luckyItemsList[index].topText
        }
    }
}