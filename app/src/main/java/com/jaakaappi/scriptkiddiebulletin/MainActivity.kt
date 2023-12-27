package com.jaakaappi.scriptkiddiebulletin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaakaappi.scriptkiddiebulletin.data.Item
import com.jaakaappi.scriptkiddiebulletin.ui.theme.CardWhite
import com.jaakaappi.scriptkiddiebulletin.ui.theme.ScriptKiddieBulletinTheme
import java.net.URI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScriptKiddieBulletinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PostList()
                }
            }
        }
    }
}

@Composable
fun PostList(itemListViewModel: ItemListViewModel = viewModel()) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        for (post in itemListViewModel.bestStoriesItems) {
            item { PostCard(post) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostCard(post: Item) {
    Row(
        Modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .background(
                CardWhite // TODO theme surface color does not work for some reason
            )
    ) {
        Column(
            Modifier
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(
                4.dp
            )
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.alignByBaseline()
                )
                if (!post.url.isNullOrBlank())
                    Text(
                        text = "(${URI(post.url).host})",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.alignByBaseline(),
                    )

            }
            Row {
                Text(text = "by ${post.by}", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = " ${
                        (System.currentTimeMillis() / 1000 - post.time) / 3600
                    } hours ago", style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScriptKiddieBulletinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PostList()
        }
    }
}