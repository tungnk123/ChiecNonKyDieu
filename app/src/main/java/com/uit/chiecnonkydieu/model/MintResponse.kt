package com.uit.chiecnonkydieu.model

data class MintResponse(
    val id: String,
    val status: Int,
    val data: MintData
)

data class MintData(
    val success: Boolean,
    val transactionHash: String
)
