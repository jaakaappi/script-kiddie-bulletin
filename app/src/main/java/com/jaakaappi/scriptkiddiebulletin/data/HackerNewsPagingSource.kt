package com.jaakaappi.scriptkiddiebulletin.data

import androidx.core.math.MathUtils.clamp
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jaakaappi.scriptkiddiebulletin.HackerNewsApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.math.max

class HackerNewsPagingSource(
    private val itemIds: List<Int>,
    private val hackerNewsService: HackerNewsApiInterface
) :
    PagingSource<Int, HackerNewsItem>() {

    // TODO error handling
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HackerNewsItem> {
        if (itemIds.isEmpty()) {
            return LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null
            )
        }

        val startIndex = params.key ?: 0
        // HackerNews best stories is 500 items long
        val endIndex = clamp(startIndex + params.loadSize, 0, 500)
        val nextItemIds = itemIds.subList(startIndex, endIndex)


        val items = withContext(Dispatchers.IO) {
            nextItemIds.map {
                async {
                    hackerNewsService.getItem(it)
                }
            }.map {
                it.await()
            }
        }

        return LoadResult.Page(
            data = items,
            prevKey = when (startIndex) {
                0 -> null
                else -> max(0, startIndex - params.loadSize)
            },
            nextKey = if (endIndex < 500) endIndex else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, HackerNewsItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        state.closestItemToPosition(anchorPosition) ?: return null
        // TODO figure the actual resume point, probably by saving the index in the class as a prop
        return 0
    }
}
