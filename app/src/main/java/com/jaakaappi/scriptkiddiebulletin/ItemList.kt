package com.jaakaappi.scriptkiddiebulletin

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.jaakaappi.scriptkiddiebulletin.data.HackerNewsItem
import com.jaakaappi.scriptkiddiebulletin.ui.theme.CardWhite
import com.jaakaappi.scriptkiddiebulletin.ui.theme.TextDarkGrey
import com.jaakaappi.scriptkiddiebulletin.ui.theme.TextLightGrey
import java.net.URI


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemList(
    itemListViewModel: ItemListViewModel = viewModel(),
    onNavigateToItemScreen: (id: Int) -> Unit
) {

    val itemPagingItems: LazyPagingItems<HackerNewsItem> =
        itemListViewModel.itemListState.collectAsLazyPagingItems()

    val isLoading =
        itemPagingItems.loadState.source.refresh == LoadState.Loading || itemListViewModel.areItemIdsLoading.value

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = itemListViewModel::refresh
    )

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            // TODO add header that can be scrolled over, try negative padding
//            item {
//                Row(
//                    Modifier
//                        .background(MaterialTheme.colorScheme.primary)
//                        .fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(text = "Script Kiddie Bulletin")
//                    IconButton(onClick = {}) {
//                        Icon(
//                            Icons.Default.MoreVert, ""
//                        )
//                    }
//                }
//            }

            items(itemPagingItems.itemCount) { index ->
                ItemCard(
                    item = itemPagingItems[index]!!,
                    onNavigateToItemScreen = onNavigateToItemScreen
                )
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemCard(
    item: HackerNewsItem,
    onNavigateToItemScreen: (id: Int) -> Unit
) {
    Row(
        Modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .background(
                CardWhite // TODO theme surface color does not work for some reason
            )
            .clickable { onNavigateToItemScreen(item.id) }
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title!!,
                    style = MaterialTheme.typography.labelLarge,
                )
                if (!item.url.isNullOrBlank()) Text(
                    text = "(${URI(item.url).host})",
                    style = MaterialTheme.typography.labelMedium,
                )

            }
            Row {
                Text(text = "by ${item.by}", style = MaterialTheme.typography.labelMedium)
                item.time?.let {
                    Text(
                        text = timestampToTimeString(item.time),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.Center
            ) {
                ItemCardIconButton(
                    Icons.Outlined.ArrowCircleUp,
                    iconContentDescription = "Upvote",
                    modifier = Modifier
                        .weight(0.2f, fill = true)
                        .fillMaxHeight(),
                    text = item.score.toString(),
                    border = true,
                )
                ItemCardIconButton(
                    Icons.Outlined.Chat,
                    iconContentDescription = "Show comments",
                    modifier = Modifier.weight(0.2f, fill = true),
                    text = item.descendants.toString(),
                    border = true,
                    onClick = { onNavigateToItemScreen(item.id) }
                )

                val context = LocalContext.current
                ItemCardIconButton(Icons.Outlined.Share,
                    iconContentDescription = "Share link to post",
                    modifier = Modifier
                        .weight(0.2f, fill = true)
                        .fillMaxHeight(),
                    border = true,
                    onClick = {
                        val shareIntent = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT, "https://news.ycombinator.com/item?id=${item.id}"
                            )
                            putExtra(Intent.EXTRA_TITLE, "HN: ${item.title}")
                            type = "text/plain"
                        }, null)
                        context.startActivity(shareIntent)
                    })
                ItemCardIconButton(
                    Icons.Outlined.OpenInNew,
                    iconContentDescription = "Open link",
                    modifier = Modifier.weight(0.2f, fill = true),
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
                    })
            }
        }
    }
}

@Composable
fun ItemCardIconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    iconContentDescription: String,
    text: String? = null,
    iconSize: Int = 16,
    border: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    var rowModifier = modifier.fillMaxHeight()

    if (border) rowModifier = rowModifier.drawBehind {
        drawLine(
            TextLightGrey, Offset(size.width, 0f), Offset(size.width, size.height), 1 * density
        )
    }

    if (onClick != null) rowModifier = rowModifier.clickable { onClick() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = rowModifier.fillMaxWidth()
    ) {
        Icon(
            imageVector,
            iconContentDescription,
            modifier = Modifier.size(iconSize.dp),
            tint = TextDarkGrey
        )
        if (!text.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text, style = MaterialTheme.typography.bodyMedium, color = TextDarkGrey
            )
        }

    }
}