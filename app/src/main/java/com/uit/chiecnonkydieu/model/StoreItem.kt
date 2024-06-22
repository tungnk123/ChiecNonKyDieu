package com.uit.chiecnonkydieu.model

import androidx.annotation.DrawableRes

data class StoreItem(
    val itemName: String,
    @DrawableRes val itemImg: Int,
    val isPurchased: Boolean,
    val itemPrice: Int
)
