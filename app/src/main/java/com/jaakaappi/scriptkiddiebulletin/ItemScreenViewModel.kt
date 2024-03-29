package com.kaappi.scriptkiddiebulletin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaappi.scriptkiddiebulletin.data.HackerNewsApiClient
import com.kaappi.scriptkiddiebulletin.data.HackerNewsApiInterface
import com.kaappi.scriptkiddiebulletin.data.HackerNewsItem
import com.kaappi.scriptkiddiebulletin.data.HackerNewsItemRepository.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemScreenViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val hackerNewsService: HackerNewsApiInterface = HackerNewsApiClient.apiService

    val item = MutableStateFlow<HackerNewsItem?>(null)
    val commentsAreLoading = MutableStateFlow(true)
    val kids = MutableStateFlow(mutableListOf<HackerNewsItem>())

    init {
        println(savedStateHandle.get<Int>("id"))
        println(repository.getFetchedItems().size)

        item.value =
            savedStateHandle.get<Int>("id")?.let { repository.getFetchedItem(it) }
                ?: HackerNewsItem(
                    0,
                    false,
                    "",
                    "",
                    0,
                    "",
                    false,
                    0,
                    0,
                    listOf(),
                    null, 0, "", listOf(), 0
                )

        item.value?.let {
            if (!it.kids.isNullOrEmpty())
                getItemKids(it)
        }
        commentsAreLoading.value = false
    }

    private fun getItemKids(item: HackerNewsItem) {
        viewModelScope.launch(Dispatchers.IO) {

            val newKids = withContext(Dispatchers.IO) {
                item.kids!!.map {
                    async {
                        hackerNewsService.getItem(it)
                    }
                }.map {
                    it.await()
                }
            }.filter { it.type == "comment" && !it.text.isNullOrBlank() && it.deleted == null && it.dead == null }
                .toMutableList()

            println(newKids)

            kids.value = newKids
        }
    }
}