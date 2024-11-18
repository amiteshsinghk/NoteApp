package com.example.notes.noteList

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notes.noteDetail.NoteDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NodeListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        viewModel.loadNotes()
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("note_detail/-1L")
//                  isBottomSheetVisible = true
                }, containerColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HideableSearchTextField(
                    text = state.searchText,
                    isSearchActive = state.isSearchActive,
                    onTextChange = viewModel::onSearchTextChange,
                    onSearchClicked = viewModel::onToggleSearch,
                    onCloseClicked = viewModel::onToggleSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )
                this@Column.AnimatedVisibility(
                    visible = !state.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()

                ) {
                    Text(
                        text = "All Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items = state.notes,
                    key = { it.id!! }) {
                    NoteItem(
                        note = it,
                        backgroundColor = Color(it.colorHex),
                        onNoteClick = {
                            navController.navigate("note_detail/${it.id}")
                        },
                        onDeleteClick = {
                            it.id?.let { it1 -> viewModel.deleteNoteById(it1) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItemPlacement()
                    )
                }
            }
        }
        if (isBottomSheetVisible) {
            val noteDetailViewModel: NoteDetailViewModel = hiltViewModel()
            val detailState by noteDetailViewModel.state.collectAsState()
            BottomSheetLayout(
                onDismissRequest = {
                    Log.d("NoteDetailViewModel", "NoteListScreen :: onDismissRequest called")
                    isBottomSheetVisible = false // Hide the bottom sheet
                },
                onTitleChange = {
                    Log.d("NoteDetailViewModel", "NoteListScreen :: onTitleChange called :: value==> $it")
                    noteDetailViewModel.onNoteTitleChange(it)
                },
                onDetailsChange = {
                    Log.d("NoteDetailViewModel", "NoteListScreen  :: onDetailsChange called :: value==> $it")
                    noteDetailViewModel.onNoteContentChange(it)},
                onSaveContent = {
                    Log.d("NoteDetailViewModel", "NoteListScreen  :: onSaveContent called")
                    noteDetailViewModel.saveNote()
                }
            )
        }

    }
}