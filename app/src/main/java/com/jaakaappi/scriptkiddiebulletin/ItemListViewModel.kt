package com.jaakaappi.scriptkiddiebulletin

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaakaappi.scriptkiddiebulletin.data.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemListViewModel() : ViewModel() {
    val hackerNewsService = HackerNewsApiCLient.apiService
    var bestStoriesIds = mutableStateListOf<Int>()
    var bestStoriesItems = mutableStateListOf<Item>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val bestStoriesIdsResponse = hackerNewsService.getBestStories()
            bestStoriesIds.addAll(bestStoriesIds)
            print("best stories ids")
            println(bestStoriesIdsResponse)

            if (bestStoriesIdsResponse.isNotEmpty()) {
                val ids = bestStoriesIdsResponse.chunked(20)[0]
                val items = ids.map {
                    async {
                        hackerNewsService.getItem(it)
                    }
                }.map {
                    it.await()
                }
                bestStoriesItems.addAll(items)
            }
        }
    }
}