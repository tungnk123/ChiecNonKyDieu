package com.example.chiecnonkydieu.data

data class GameModel(
    val gameId: Int = -1,
    val winner: String = "",
    var gameStatus: GameStatus = GameStatus.CREATED,
    val playersList: MutableList<Player> = mutableListOf<Player>(),
    val currentPlayer: Player = Player(),
    val currentWord: String = ""

)

enum class GameStatus {
    CREATED,
    JOINED1,
    JOINED2,
    INPROGRESS,
    FINISHED
}
