package com.uit.chiecnonkydieu

import com.uit.chiecnonkydieu.network.ApillonApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private val BASE_URL = "https://api.apillon.io/"

    val api: ApillonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApillonApiService::class.java)
    }
}