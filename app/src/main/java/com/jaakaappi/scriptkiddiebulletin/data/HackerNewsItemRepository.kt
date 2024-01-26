package com.jaakaappi.scriptkiddiebulletin.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class ItemRepository {
    private var fetchedItems: List<HackerNewsItem> = listOf()

    fun getFetchedItem(id: Int): HackerNewsItem? {
        return fetchedItems.find { it.id == id }
    }

    fun getFetchedItems(): List<HackerNewsItem> {
        return fetchedItems
    }

    fun addFetchedItems(newItems: List<HackerNewsItem>) {
        fetchedItems = (fetchedItems + newItems).distinctBy { it.id }
    }

    fun getPagedItems(
        itemIds: List<Int>,
        hackerNewsService: HackerNewsApiInterface
    ): Flow<PagingData<HackerNewsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 50, enablePlaceholders = false),
            pagingSourceFactory = { HackerNewsPagingSource(itemIds, hackerNewsService) }
        )
            .flow
    }
}

object HackerNewsItemRepository {
    val repository = ItemRepository()
}