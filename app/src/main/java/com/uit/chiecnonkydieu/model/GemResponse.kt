package com.uit.chiecnonkydieu.model

data class GemResponse(
    val username: String,
    val coins: Int,
    val playerItems: List<PlayerItem>
)

data class PlayerItem(
    val playerId: Int,
    val itemId: Int,
    val quantity: Int,
    val obtainedTime: String
)
