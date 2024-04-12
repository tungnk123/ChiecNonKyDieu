package com.example.chiecnonkydieu.ui.playingRoom

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.data.GameStatus
import com.example.chiecnonkydieu.data.model.LetterCard
import com.example.chiecnonkydieu.data.model.Player
import com.example.chiecnonkydieu.data.model.QuestionAnswer

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
                GameData.saveGameModel(gameModel)
                return true
            }
            else {
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

    private fun changeTurn(player: Player) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.currentPlayer = player
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
        GameData.saveGameModel(gameModel)
    }

    fun makeAllLetterCardReveal() {
        for (i in gameModel.letterCardList.indices) {
            if (gameModel.letterCardList[i].isHidden) {
                gameModel.letterCardList[i].isHidden = false
            }
        }
        GameData.saveGameModel(gameModel)
    }

    fun checkGameWin(): Boolean {
        return true
    }


}