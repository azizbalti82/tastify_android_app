package com.balti.tastify.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Instance {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter to parse JSON
            .build()
    }
}
