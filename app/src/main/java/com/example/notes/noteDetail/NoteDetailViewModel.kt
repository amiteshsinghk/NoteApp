package com.example.notes.noteDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydatabase.domain.note.Note
import com.example.mydatabase.domain.note.NoteDataSource
import com.example.mydatabase.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteTitle = savedStateHandle.getStateFlow("noteTitle", "")
    private val isNoteTitleIsFocused = savedStateHandle.getStateFlow("isNoteTitleIsFocused", false)
    private val noteContent = savedStateHandle.getStateFlow("noteContent", "")
    private val isNoteContentIsFocused = savedStateHandle.getStateFlow("isNoteContentIsFocused", false)
    private val noteColor = savedStateHandle.getStateFlow("noteColor", Note.generateRandomColor())

    val state = combine(
        noteTitle,
        isNoteTitleIsFocused,
        noteContent,
        isNoteContentIsFocused,
        noteColor){noteTitle, isNoteTitleIsFocused, noteContent, isNoteContentIsFocused, noteColor ->
        NoteDetailState(
            noteTitle = noteTitle,
            isNoteTitleHintVisible = noteTitle.isEmpty() && !isNoteTitleIsFocused,
            noteContent = noteContent,
            isNoteContentHintVisible = noteContent.isEmpty() && !isNoteContentIsFocused,
            noteColor = noteColor
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    private val _hasNoteSaved = MutableStateFlow(false)
    val hasNoteSaved = _hasNoteSaved.asStateFlow()

    private var existingNoteId: Long? = null

    init {
        savedStateHandle.get<Long>("noteId")?.let {
            if (it == -1L) return@let
            existingNoteId = it
            viewModelScope.launch {
                noteDataSource.getNodeById(it)?.let {note->
                    savedStateHandle["noteTitle"] = note.title
                    savedStateHandle["noteContent"] = note.content
                    savedStateHandle["noteColor"] = note.colorHex
                }
            }
        }
    }

    fun onNoteTitleChange(text: String){
        savedStateHandle["noteTitle"] = text
    }
    fun onNoteContentChange(text: String){
        savedStateHandle["noteContent"] = text
    }
    fun onNoteTitleFocusChange(isFocused: Boolean){
        savedStateHandle["isNoteTitleIsFocused"] = isFocused
    }
    fun onNoteContentFocusChange(isFocused: Boolean){
        savedStateHandle["isNoteContentIsFocused"] = isFocused
    }
    fun saveNote() {
        viewModelScope.launch {
            if (state.value.noteTitle.isNotEmpty() || state.value.noteContent.isNotEmpty()){
                noteDataSource.insertNode(
                    Note(
                        id = existingNoteId,
                        title = if(noteTitle.value.isNotEmpty()) noteTitle.value else noteContent.value.split(" ")[0],
                        content = noteContent.value,
                        colorHex = noteColor.value,
                        created = DateTimeUtil.now()
                    )
                )
            }
            _hasNoteSaved.value = true
        }
    }
}