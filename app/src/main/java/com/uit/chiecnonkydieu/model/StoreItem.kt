package com.uit.chiecnonkydieu.model

import androidx.annotation.DrawableRes

data class StoreItem(
    val itemName: String,
    var itemImg: String,
    var isPurchased: Boolean,
    val itemPrice: Int,
    val itemId: Int = 23
)
