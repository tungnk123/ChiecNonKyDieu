package com.uit.chiecnonkydieu.ui.wheel

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uit.chiechnonkydieu.R
import com.uit.chiecnonkydieu.data.GameData
import com.uit.chiecnonkydieu.data.GameModel
import com.uit.chiecnonkydieu.data.GameStatus
import com.uit.chiecnonkydieu.model.LetterCard
import rubikstudio.library.model.LuckyItem
class EveryDayWheelViewModel: ViewModel() {
    private val _luckyItemsList: MutableList<LuckyItem> = mutableListOf()
    val luckyItemsList:List<LuckyItem> = _luckyItemsList

    private val _letterCardList = MutableLiveData<MutableList<LetterCard>>()
    val letterCardList: LiveData<MutableList<LetterCard>> = _letterCardList
    fun initLuckyItemList(context: Context) {
        val luckyItem: LuckyItem = LuckyItem()
        luckyItem.secondaryText = "1"
        luckyItem.color = ContextCompat.getColor(context, R.color.purple_300)
        _luckyItemsList.add(luckyItem)

        val luckyItem2: LuckyItem = LuckyItem()
        luckyItem2.secondaryText = "2"
        luckyItem2.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem2)

        val luckyItem3: LuckyItem = LuckyItem()
        luckyItem3.secondaryText = "3"
        luckyItem3.color = ContextCompat.getColor(context, R.color.blue_200)
        _luckyItemsList.add(luckyItem3)

        val luckyItem4: LuckyItem = LuckyItem()
        luckyItem4.secondaryText = "4"
        luckyItem4.color = ContextCompat.getColor(context, R.color.orange)
        _luckyItemsList.add(luckyItem4)

        val luckyItem5: LuckyItem = LuckyItem()
        luckyItem5.secondaryText = "5"
        luckyItem5.color = ContextCompat.getColor(context, R.color.gray)
        _luckyItemsList.add(luckyItem5)

        val luckyItem6: LuckyItem = LuckyItem()
        luckyItem6.secondaryText = "6"
        luckyItem6.color = ContextCompat.getColor(context, R.color.blue_200)

        _luckyItemsList.add(luckyItem6)


        val luckyItem8: LuckyItem = LuckyItem()
        luckyItem8.secondaryText = "7"
        luckyItem8.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem8)

        val luckyItem9: LuckyItem = LuckyItem()
        luckyItem9.secondaryText = "8"
        luckyItem9.color = ContextCompat.getColor(context, R.color.purple_300)
        _luckyItemsList.add(luckyItem9)

        val luckyItem10: LuckyItem = LuckyItem()
        luckyItem10.secondaryText = "9"
        luckyItem10.color = ContextCompat.getColor(context, R.color.orange)
        _luckyItemsList.add(luckyItem10)

        val luckyItem11: LuckyItem = LuckyItem()
        luckyItem11.secondaryText = "10"
        luckyItem11.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem11)

        val luckyItem12: LuckyItem = LuckyItem()
        luckyItem12.secondaryText = "11"
        luckyItem12.color = ContextCompat.getColor(context, R.color.purple_300)
        _luckyItemsList.add(luckyItem12)

        val luckyItem13: LuckyItem = LuckyItem()
        luckyItem13.secondaryText = "12"
        luckyItem13.color = ContextCompat.getColor(context, R.color.blue_200)
        _luckyItemsList.add(luckyItem13)

        val luckyItem14: LuckyItem = LuckyItem()
        luckyItem14.secondaryText = "13"
        luckyItem14.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem14)

        val luckyItem15: LuckyItem = LuckyItem()
        luckyItem15.secondaryText = "14"
        luckyItem15.color = ContextCompat.getColor(context, R.color.blue_200)
        _luckyItemsList.add(luckyItem15)

        val luckyItem16: LuckyItem = LuckyItem()
        luckyItem16.secondaryText = "15"
        luckyItem16.color = ContextCompat.getColor(context, R.color.green)
        _luckyItemsList.add(luckyItem16)

        val luckyItem17: LuckyItem = LuckyItem()
        luckyItem17.secondaryText = "16"
        luckyItem17.color = ContextCompat.getColor(context, R.color.purple_300)
        _luckyItemsList.add(luckyItem17)
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

    fun updateStatusGameModel(gameStatus: GameStatus) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.gameStatus = gameStatus
        }
    }

    private fun changeTurn() {
        val gameModel: GameModel? = GameData.gameModel.value
        var indexNewPlayer = -1
        gameModel?.let {
            for (i in gameModel.playersList.indices) {
                if (gameModel.playersList[i].name == gameModel.currentPlayer.name) {
                    indexNewPlayer = (i + 1) % gameModel.playersList.size
                }
            }
            gameModel.currentPlayer = gameModel.playersList[indexNewPlayer]
        }
    }

    fun updateCurrentSpinValue(spinValue: String) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.currentSpinValue = spinValue
            if (spinValue.isDigitsOnly() || spinValue == "ITEM") {
                updateStatusGameModel(GameStatus.GUESS)
            }
            else if (spinValue == "MISS TURN"){
                updateStatusGameModel(GameStatus.INPROGRESS)
                changeTurn()
            }
            GameData.saveGameModel(gameModel)
        }

    }
}