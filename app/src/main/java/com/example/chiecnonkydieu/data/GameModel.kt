package com.example.chiecnonkydieu.data

import com.example.chiecnonkydieu.model.LetterCard
import com.example.chiecnonkydieu.model.Player
import com.example.chiecnonkydieu.model.QuestionAnswer

data class GameModel(
    val gameId: Int = -1,
    val winner: String = "",
    var gameStatus: GameStatus = GameStatus.CREATED,
    val playersList: MutableList<Player> = mutableListOf<Player>(),
    var currentPlayer: Player = Player(),
    var currentQuestionAnswer: QuestionAnswer = questionAnswerList[0],
    val guessesCharacters: MutableList<String> = mutableListOf(),
    var letterCardList: MutableList<LetterCard> = mutableListOf(),
    val previousQuestionAnswers: MutableList<QuestionAnswer> = mutableListOf(),
    var currentSpinValue: String = ""

)

enum class GameStatus {
    CREATED,
    JOINED1,
    JOINED2,
    INPROGRESS,
    GUESS,
    ENDROUND,
    WAITING_TO_CONTINUE,
    FINISHED
}
