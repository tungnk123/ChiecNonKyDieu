package com.uit.chiecnonkydieu.model

import java.time.LocalDateTime

data class PlayerDto(
    val username: String,
    val passwordHash: String,
    val digCount: Int,
    val gemTypeCount: Int,
    val coins: Int,
    val lastCoins: Int,
    val lastCoinsTime: String,
    val lastDigTime: String,
    val playerItems: String,
    val id: Int,
    val createdAt: String,
    val lastModifiedAt: String,
)

fun PlayerDto.toLeaderboardItem(rank: Int): LeaderboardItem {
    return LeaderboardItem(
        rank,
        this.username,
        this.coins
    )
}