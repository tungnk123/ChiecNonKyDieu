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
import com.example.chiecnonkydieu.data.REFERENCE_CAUHOI
import com.example.chiecnonkydieu.data.database
import com.example.chiecnonkydieu.data.questionAnswerList
import com.example.chiecnonkydieu.model.LetterCard
import com.example.chiecnonkydieu.model.Player
import com.example.chiecnonkydieu.model.QuestionAnswer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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
        getQuestionFromFirebase()
    }

    private fun getQuestionFromFirebase() {
        val questionAnswerListFromFirebase: MutableList<QuestionAnswer> = mutableListOf()

        // Lắng nghe sự kiện khi dữ liệu thay đổi trên Firebase
        database.child(REFERENCE_CAUHOI).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Xóa danh sách hiện tại để cập nhật với dữ liệu mới
                questionAnswerListFromFirebase.clear()

                // Lặp qua các nút con của nút "QUESTION_ANSWER" trên Firebase
                for (snapshot in dataSnapshot.children) {
                    // Lấy dữ liệu từ DataSnapshot và chuyển đổi thành đối tượng QuestionAnswer
                    val questionAnswer = snapshot.getValue(QuestionAnswer::class.java)
                    if (questionAnswer != null) {
                        questionAnswerListFromFirebase.add(questionAnswer)
                    }
                }

                // Gán danh sách câu hỏi và câu trả lời từ Firebase vào questionAnswerList
                questionAnswerList.clear()
                questionAnswerList.addAll(questionAnswerListFromFirebase)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        })
    }


    fun isUserInputMatch(playerInputChar: Char): Boolean {
        val gameModel: GameModel? = GameData.gameModel.value
        val playerInputStringLC = playerInputChar.lowercaseChar().toString()
        val playerInputCharLC = playerInputChar.lowercaseChar()
        gameModel?.let {
            if (gameModel.currentQuestionAnswer.DoiTuongInHoa.contains(
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
                if (checkRoundWin()) {
                    updateStatusGameModel(GameStatus.ENDROUND)
                }
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
                for (i in questionAnswer.DoiTuongInHoa.indices) {
                    tempLetterCardList.add(LetterCard(questionAnswer.DoiTuongInHoa[i].toString()))
                }
                gameModel.letterCardList = tempLetterCardList
                gameModel.gameStatus = GameStatus.INPROGRESS
                gameModel.guessesCharacters.clear()
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
            if (currentSpinValue.isDigitsOnly()) {
                gameModel.playersList[index].score += currentSpinValue.toInt()
            } else if (currentSpinValue == "ITEM"){
                // TODO
            } else if (currentSpinValue == "MISS TURN") {
                changeTurn()
                updateStatusGameModel(GameStatus.INPROGRESS)
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
        val gameModel: GameModel? = GameData.gameModel.value
        var count = 0
        gameModel?.let {

            for (i in gameModel.letterCardList.indices) {
                if (gameModel.letterCardList[i].isHidden) {
                    count++
                }
            }

        }
        return count == 0
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