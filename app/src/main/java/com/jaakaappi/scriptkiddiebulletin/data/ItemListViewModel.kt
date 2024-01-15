package com.jaakaappi.scriptkiddiebulletin.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jaakaappi.scriptkiddiebulletin.HackerNewsApiCLient
import com.jaakaappi.scriptkiddiebulletin.HackerNewsApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ItemListViewModel : ViewModel() {
    // TODO lifecycle state
    private val hackerNewsService: HackerNewsApiInterface = HackerNewsApiCLient.apiService
    private val hackerNewsItemRepository = HackerNewsItemRepository.repository
    val isLoading = mutableStateOf(true)


    val itemListState: MutableStateFlow<PagingData<HackerNewsItem>> =
        MutableStateFlow(value = PagingData.empty())

    init {
        getBestStories()
    }

    fun getBestStories() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val bestStoriesIdsResponse = hackerNewsService.getBestStories()
            println(bestStoriesIdsResponse)

            hackerNewsItemRepository.getPagedItems(bestStoriesIdsResponse, hackerNewsService)
                .cachedIn(viewModelScope)
                .collect { itemListState.value = it }
        }
        isLoading.value = false
    }
}