package com.example.chiecnonkydieu.ui.playingRoom

import androidx.lifecycle.ViewModel
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.data.Player
import java.util.Locale

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
            if (gameModel.currentAnswer.contains(playerInputStringLC, ignoreCase = true) &&
                ! gameModel.guessesCharacters.contains(playerInputStringLC)) {
                saveGuessedChar(playerInputCharLC)
                gameModel.guessesCharacters.add(playerInputStringLC)
                gameModel.letterCardList.filter {
                    it.letter.toLowerCase() == playerInputStringLC
                }
                    .forEach{
                        it.isHidden = false
                }
                GameData.saveGameModel(gameModel)
                return true
            }
            else {
                return false;
            }

        }

        return false
    }

    fun setQuestionAndCurrentWordToBeGuessed(): Boolean {
        return false
    }

    private fun saveGuessedChar(playerInputChar: Char) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.guessesCharacters.add(playerInputChar.toString())
        }
    }


    private fun updateScore(indexPlayer: Int, score: Int) {
        val gameModel: GameModel? = GameData.gameModel.value
        gameModel?.let {
            gameModel.playersList[indexPlayer].score += score
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

    private fun checkRoundWin(): Boolean {
        return true
    }
    private fun checkGameWin(): Boolean {
        return true
    }


}