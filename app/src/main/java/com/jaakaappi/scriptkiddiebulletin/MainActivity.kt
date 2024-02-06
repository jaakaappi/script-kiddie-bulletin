package com.kaappi.scriptkiddiebulletin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaappi.scriptkiddiebulletin.ui.theme.ScriptKiddieBulletinTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ScriptKiddieBulletinTheme {
                NavHost(navController = navController, startDestination = "itemList") {
                    composable("itemList") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            ItemList(onNavigateToItemScreen = { id ->
                                navController.navigate(
                                    "itemScreen/$id"
                                )
                            })
                        }
                    }
                    composable(
                        "itemScreen/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { ItemScreen() }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScriptKiddieBulletinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
//            PostList()
        }
    }
}