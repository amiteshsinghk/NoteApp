package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.noteDetail.NoteDetailScreen
import com.example.notes.noteList.NodeListScreen
import com.example.notes.noteList.NoteListState
import com.example.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "note_list") {
                    composable(route = "note_list") {
                        NodeListScreen(navController = navController)
                    }
                    composable(
                        route = "note_detail/{noteId}",
                        arguments = listOf(
                            navArgument(name = "noteId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
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


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NotesTheme {
//        Greeting("Android")
//    }
//}