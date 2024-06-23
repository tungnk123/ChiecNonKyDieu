package com.uit.chiecnonkydieu.network

import com.uit.chiecnonkydieu.model.GemResponse
import com.uit.chiecnonkydieu.model.Player
import com.uit.chiecnonkydieu.model.PlayerDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GemApiService {
    @GET("username")
    fun getItems(
        @Query("username") username: String
    ): GemResponse

    @POST("DigGem")
    fun digGem(
        @Query("username") username: String,
        @Query("row") row: Int,
        @Query("col") col: Int
    ): Unit

    @GET("GetAllPlayers")
    suspend fun getAllPlayers(): List<PlayerDto>
}