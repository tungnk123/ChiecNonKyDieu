package com.uit.chiecnonkydieu.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ApillonApiService {
    @GET("endpoint/{id}")
    fun getItem(@Path("id") id: String)

}