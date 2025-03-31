package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.notes.noteDetail.NoteDetailScreen
import com.example.notes.noteList.NodeListScreen
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.util.AppConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = AppConstant.NODE_LIST) {
                    composable(route = AppConstant.NODE_LIST,
                        deepLinks = listOf(navDeepLink { uriPattern = "${AppConstant.MY_APP_URL}${AppConstant.NODE_LIST}" })
                        ) {
                        NodeListScreen(navController = navController)
                    }
                    composable(
                        route = "${AppConstant.NODE_DETAIL}/{${AppConstant.NOTE_ID}}",
                        arguments = listOf(
                            navArgument(name = AppConstant.NOTE_ID) {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        ),
                        deepLinks = listOf(navDeepLink { uriPattern = "${AppConstant.MY_APP_URL}${AppConstant.NODE_DETAIL}" })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getLong(AppConstant.NOTE_ID) ?: -1L
                        NoteDetailScreen(noteId = noteId, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}