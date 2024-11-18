package com.example.mydatabase.domain.note

import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun insertNode(note: Note)
    suspend fun getNodeById(id: Long): Note?
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun deleteNoteById(id: Long)
}