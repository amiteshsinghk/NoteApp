package com.example.notes.noteDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun NoteDetailScreen(
    navController: NavController,
    noteId: Long? = null,
    viewModel: NoteDetailViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()
    val hasNoteSaved by viewModel.hasNoteSaved.collectAsState()
     LaunchedEffect(key1 = hasNoteSaved){
         if (hasNoteSaved){
             navController.popBackStack()
         }
     }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::saveNote,
                containerColor = Color.Black,
                shape = CircleShape

            ){
                Icon(imageVector = Icons.Default.Check,
                    contentDescription = "Save Note",
                    tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .background(Color(state.noteColor))
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
        ) {
            TransparentHintTextField(
                text = state.noteTitle,
                hint = "Enter a title...",
                isHintVisible = state.isNoteTitleHintVisible,
                onValueChanged = viewModel::onNoteTitleChange,
                onFocusChanged = {
                    viewModel.onNoteTitleFocusChange(it.isFocused)},
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = state.noteContent,
                hint = "Enter content...",
                isHintVisible = state.isNoteContentHintVisible,
                onValueChanged = viewModel::onNoteContentChange,
                onFocusChanged = {
                    viewModel.onNoteContentFocusChange(it.isFocused)},
                singleLine = false,
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}