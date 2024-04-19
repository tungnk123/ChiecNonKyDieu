package com.example.chiecnonkydieu.ui.playingRoom

import android.content.Context
import androidx.core.text.isDigitsOnly
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameData.gameModel
import com.example.chiecnonkydieu.data.GameData.saveGameModel
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.data.GameStatus
import com.example.chiecnonkydieu.model.LetterCard
import com.example.chiecnonkydieu.model.Player
import com.example.chiecnonkydieu.model.QuestionAnswer
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch



class PlayingRoomViewModel: ViewModel() {

    private lateinit var gameModel: GameModel


    init {
        if (GameData.gameModel.value != null) {
            gameModel = GameData.gameModel.value!!
        }
    }

    fun isUserInputMatch(playerInputChar: Char): Boolean {
        val gameModel: GameModel? = GameData.gameModel.value
        val playerInputStringLC = playerInputChar.lowercaseChar().toString()
        val playerInputCharLC = playerInputChar.lowercaseChar()
        gameModel?.let {
            if (gameModel.currentQuestionAnswer.answer.contains(
                    playerInputStringLC,
                    ignoreCase = true
                ) &&
                ! gameModel.guessesCharacters.contains(playerInputStringLC)) {
                saveGuessedChar(playerInputCharLC)
                gameModel.letterCardList.filter {
                    it.letter.toLowerCase() == playerInputStringLC
                }
                    .forEach{
                        it.isHidden = false
                }
                updateStatusGameModel(GameStatus.INPROGRESS)
                updateScore(gameModel)
                changeTurn()
                GameData.saveGameModel(gameModel)
                return true
            }
            else {
                updateStatusGameModel(GameStatus.INPROGRESS)
                changeTurn()
                GameData.saveGameModel(gameModel)
                return false;
            }
        }
        return false
    }

    fun setQuestionAndCurrentWordToBeGuessed(questionAnswer: QuestionAnswer): Boolean {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            if (!gameModel.previousQuestionAnswers.contains(questionAnswer)) {
                gameModel.previousQuestionAnswers.add(questionAnswer)
                gameModel.currentQuestionAnswer = questionAnswer
                val tempLetterCardList = mutableListOf<LetterCard>()
                for (i in questionAnswer.answer.indices) {
                    tempLetterCardList.add(LetterCard(questionAnswer.answer[i].toString()))
                }
                gameModel.letterCardList = tempLetterCardList
                resetGuessedCharacter()
                GameData.saveGameModel(gameModel)
                return true
            }
            return false
        }
        return false
    }

    private fun saveGuessedChar(playerInputChar: Char) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.guessesCharacters.add(playerInputChar.toString())
        }
    }

    fun updateStatusGameModel(gameStatus: GameStatus) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.gameStatus = gameStatus
        }
    }

    fun updateScore(gameModel: GameModel) {
        // TODO xu ly item, extra turn, miss turn
        gameModel.let {
            val currentPlayer = gameModel.currentPlayer
            val currentSpinValue = gameModel.currentSpinValue
            val index = gameModel.playersList.indexOfFirst { it.name == currentPlayer.name }
            gameModel.playersList[index].score += currentSpinValue.toInt()
            if (currentSpinValue.isDigitsOnly()) {

            } else {

            }
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

    private fun nextRound() {
        // TODO
    }


    fun checkRoundWin(): Boolean {
        for (i in gameModel.letterCardList.indices) {
            if (gameModel.letterCardList[i].isHidden) {
                return false
            }
        }
        return true

    }

    fun makeAllLetterCardReveal() {
        for (i in gameModel.letterCardList.indices) {
            if (gameModel.letterCardList[i].isHidden) {
                gameModel.letterCardList[i].isHidden = false
            }
        }
//        GameData.saveGameModel(gameModel)
    }
    fun resetGuessedCharacter() {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.guessesCharacters.clear()
        }
    }
    fun updateGameEndRound() {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.gameStatus = GameStatus.ENDROUND
            GameData.saveGameModel(gameModel)
        }
    }

}