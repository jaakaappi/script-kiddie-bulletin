package com.jaakaappi.scriptkiddiebulletin.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jaakaappi.scriptkiddiebulletin.HackerNewsApiInterface
import kotlinx.coroutines.flow.Flow

class ItemRepository {
    fun getItems(
        itemIds: List<Int>,
        hackerNewsService: HackerNewsApiInterface
    ): Flow<PagingData<HackerNewsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { HackerNewsPagingSource(itemIds, hackerNewsService) }
        )
            .flow
    }
}

object HackerNewsItemRepository {
    val repository = ItemRepository()
}