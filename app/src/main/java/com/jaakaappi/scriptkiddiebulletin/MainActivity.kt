package com.jaakaappi.scriptkiddiebulletin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jaakaappi.scriptkiddiebulletin.ui.theme.ScriptKiddieBulletinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScriptKiddieBulletinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}

data class Post(val title: String)

val posts = arrayOf(Post("Ask HN: why cows fly"),Post("Show HN: dogs fly"))

@Composable
fun PostList(){
    LazyColumn{
        for (post in posts){
            item { PostCard(post) }
        }
    }
}

@Composable
fun PostCard(post: Post){
    Card {
        Text(text = post.title)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScriptKiddieBulletinTheme {
        PostList()
    }
}