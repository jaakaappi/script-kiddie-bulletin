package com.jaakaappi.scriptkiddiebulletin.data

import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsApiInterface {
    @GET("topstories.json")
    suspend fun getBestStories(): List<Int>

    @GET("item/{id}.json")
    suspend fun getItem(@Path("id") id: Int): HackerNewsItem
}