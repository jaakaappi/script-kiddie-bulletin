package com.kaappi.scriptkiddiebulletin

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaappi.scriptkiddiebulletin.data.HackerNewsItem
import com.kaappi.scriptkiddiebulletin.ui.theme.CardWhite
import com.kaappi.scriptkiddiebulletin.ui.theme.TextBlack
import java.net.URI

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemScreen(itemScreenViewModel: ItemScreenViewModel = viewModel()) {
    val item by itemScreenViewModel.item.collectAsState()
    val kids by itemScreenViewModel.kids.collectAsState()
    val commentsAreLoading by itemScreenViewModel.commentsAreLoading.collectAsState()

    println(item)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            item?.let {
                ItemScreenItemCard(item!!)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = if (!item!!.kids.isNullOrEmpty()) "${item!!.kids!!.size} comments" else "No comments")
                if (commentsAreLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(64.dp))
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else if (!item!!.kids.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(kids) { kid ->
                            if (!kid.text.isNullOrEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .background(CardWhite)
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.labelMedium,
                                        text = "${kid.by}${kid.time?.let { timestampToTimeString(kid.time) }}"
                                    )
                                    AndroidView(
                                        factory = { context ->
                                            TextView(context)
                                        },
                                        update = {
                                            it.text = HtmlCompat.fromHtml(
                                                kid.text,
                                                HtmlCompat.FROM_HTML_MODE_COMPACT
                                            )
                                            it.movementMethod = LinkMovementMethod.getInstance()
                                            it.setTextColor(TextBlack.toArgb())
                                            it.textSize = 14.sp.value
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemScreenItemCard(item: HackerNewsItem) {
    Row(
        Modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .background(
                CardWhite // TODO theme surface color does not work for some reason
            )
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                item.title?.let {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                if (!item.url.isNullOrBlank()) Text(
                    text = "(${URI(item.url).host})",
                    style = MaterialTheme.typography.labelMedium,
                )

            }
            Row {
                Text(text = "by ${item.by}", style = MaterialTheme.typography.labelMedium)
                item.time?.let {
                    Text(
                        text =
                        timestampToTimeString(item.time),
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

                val context = LocalContext.current
                ItemCardIconButton(
                    Icons.Outlined.Share,
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