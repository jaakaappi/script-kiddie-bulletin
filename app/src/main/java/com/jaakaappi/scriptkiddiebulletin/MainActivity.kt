package com.jaakaappi.scriptkiddiebulletin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jaakaappi.scriptkiddiebulletin.ui.theme.CardWhite
import com.jaakaappi.scriptkiddiebulletin.ui.theme.ScriptKiddieBulletinTheme

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

data class Post(
    val title: String,
    val url: String,
    val user: String,
    val timestamp: Int,
    val likes: Int
)

val posts =
    arrayOf(
        Post("Ask HN: why cows fly", "hackernews.com", "jaakaappi", 2, 12),
        Post("Show HN: dogs fly", "hackernews.com", "jaakaappi", 26, 46)
    )

@Composable
fun PostList() {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        for (post in posts) {
            item { PostCard(post) }
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Row(
        Modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .background(
                CardWhite // TODO theme surface color does not work for some reason
            )
    ) {
        Column(
            Modifier
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = post.title, style = MaterialTheme.typography.labelLarge)
                Text(text = " (${post.url})", style = MaterialTheme.typography.labelMedium)
            }
            Row {
                Text(text = "by ${post.user}", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = " ${
                        post.timestamp
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