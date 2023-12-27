package com.jaakaappi.scriptkiddiebulletin

import com.jaakaappi.scriptkiddiebulletin.data.Item
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsApiInterface {
    @GET("beststories.json")
    suspend fun getBestStories(): List<Int>

    @GET("item/{id}.json")
    suspend fun getItem(@Path("id") id: Int): Item
}