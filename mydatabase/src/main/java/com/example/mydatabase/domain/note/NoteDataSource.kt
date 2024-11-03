package com.example.mydatabase.domain.note

interface NoteDataSource {
    suspend fun insertNode(note: Note)
    suspend fun getNodeById(id: Long): Note?
    suspend fun getAllNotes(): List<Note>
    suspend fun deleteNoteById(id: Long)
}