package com.jaakaappi.scriptkiddiebulletin

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    val isLoading = mutableStateOf(false)

    init {
        loadStories()
    }

    fun loadStories() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            val bestStoriesIdsResponse = hackerNewsService.getBestStories()
            isLoading.value = false
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