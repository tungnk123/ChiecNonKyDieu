package com.uit.chiecnonkydieu.network

import com.uit.chiecnonkydieu.model.DigGemResponse
import com.uit.chiecnonkydieu.model.GemResponse
import com.uit.chiecnonkydieu.model.ItemMarketDto
import com.uit.chiecnonkydieu.model.Player
import com.uit.chiecnonkydieu.model.PlayerDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GemApiService {
    @GET("username")
    fun getItems(
        @Query("username") username: String
    ): Call<GemResponse>

    @POST("DigGem")
    fun digGem(
        @Query("username") username: String,
        @Query("row") row: Int,
        @Query("column") col: Int
    ): Call<DigGemResponse>

    @GET("GetAllPlayers")
    suspend fun getAllPlayers(): List<PlayerDto>

    @GET("GetAllListings")
    fun getAllListings(): Call<List<ItemMarketDto>>

    @GET("SellItem")
    fun sellItems(
        @Query("username") username: String,
        @Query("password") password: String = "123456",
        @Query("itemId") itemId: Int,
        @Query("quantity") quantity: Int,
        @Query("price") price: Int,
    ): Call<Void>

}