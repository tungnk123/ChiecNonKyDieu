package com.uit.chiecnonkydieu.network

import com.uit.chiecnonkydieu.model.MintResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApillonApiService {
    @POST("nfts/collections/{uuid}/mint")
    fun mintNFT(
        @Path("uuid") uuid: String,
        @Header("Authorization") authorization: String,
        @Body request: MintRequest
    ): Call<MintResponse>

}

data class MintRequest(val receivingAddress: String, val quantity: Int)