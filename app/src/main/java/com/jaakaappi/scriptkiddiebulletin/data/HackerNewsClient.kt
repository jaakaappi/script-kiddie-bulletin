package com.kaappi.scriptkiddiebulletin.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

    fun getClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

object HackerNewsApiClient {
    val apiService: HackerNewsApiInterface =
        RetrofitClient.getClient().create(HackerNewsApiInterface::class.java)
}