package com.example.notes.noteList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydatabase.domain.note.Note
import com.example.mydatabase.domain.note.NoteDataSource
import com.example.mydatabase.domain.note.SearchNotesUseCase
import com.example.mydatabase.domain.time.DateTimeUtil
import com.example.mydatabase.presentation.RedOrangeHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val searchNotes = SearchNotesUseCase()
    val notes = savedStateHandle.getStateFlow("notes", emptyList<Note>())
    val searchText = savedStateHandle.getStateFlow("searchText", "")
    val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    init {
        viewModelScope.launch {
            (1..10).forEach {
                noteDataSource.insertNode(
                    Note(
                        id = null,
                        title = "Note $it",
                        content = "content $it",
                        colorHex = RedOrangeHex,
                        created = DateTimeUtil.now()
                    )
                )
            }
        }
    }

    val state = combine(notes,searchText,isSearchActive){notes,searchText,isSearchActive ->
        NoteListState(
            notes = searchNotes.execute(notes,searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    fun loadNotes(){
        viewModelScope.launch {
            savedStateHandle["notes"] = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String){
        savedStateHandle["searchText"] = text
    }

    fun onToggleSearch(){
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if (!isSearchActive.value){
            savedStateHandle["searchText"] = ""
        }
    }

    fun deleteNoteById(id: Long){
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }
}