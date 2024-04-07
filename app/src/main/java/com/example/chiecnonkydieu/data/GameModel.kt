package com.example.chiecnonkydieu.data

import android.health.connect.datatypes.units.Length

data class GameModel(
    val gameId: Int = -1,
    val winner: String = "",
    var gameStatus: GameStatus = GameStatus.CREATED,
    val playersList: MutableList<Player> = mutableListOf<Player>(),
    val currentPlayer: Player = Player(),
    val currentQuestion: String = "",
    val currentAnswer: String = "HA NOI",
    val currentGuess: String = " A N  "

)

enum class GameStatus {
    CREATED,
    JOINED1,
    JOINED2,
    INPROGRESS,
    FINISHED
}
