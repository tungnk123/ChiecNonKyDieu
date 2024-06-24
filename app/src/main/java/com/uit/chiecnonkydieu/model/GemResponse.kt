package com.uit.chiecnonkydieu.model

data class GemResponse(
    val id: Int,
    val username: String,
    val coins: Int,
    val playerItems: List<PlayerItem>
)
data class PlayerItem(
    val itemId: Int,
    val itemDto: Item,
    val quantity: Int,
)


data class Item(
    val id: Int,
    val name: String,
    val dropRate: Double,
    val imageUrl: String
)

fun Item.toStoreItem(): StoreItem {
    return StoreItem(
        this.name,
        this.imageUrl,
        isPurchased = false,
        100, // TODO
        this.id
    )
}
