package com.uit.chiecnonkydieu.model

import androidx.annotation.DrawableRes

data class StoreItem(
    val itemName: String,
    @DrawableRes val itemImg: Int,
    var isPurchased: Boolean,
    val itemPrice: Int
)
