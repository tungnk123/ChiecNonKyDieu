package com.uit.chiecnonkydieu.model

data class ItemMarketDto(
    val itemName: String,
    val itemDropRate: Double,
    val itemImageUrl: String,
    val sellerUsername: String,
    val quantity: Int,
    val price: Double,
    val listedTime: String
)

fun ItemMarketDto.toStoreItem(): StoreItem {
    return StoreItem(
        this.itemName,
        this.itemImageUrl,
        isPurchased = false,
        this.price.toInt(),
    )
}
