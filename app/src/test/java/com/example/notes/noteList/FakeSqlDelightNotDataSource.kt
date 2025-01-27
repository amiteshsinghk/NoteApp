package com.example.notes.noteList

import com.example.mydatabase.domain.note.Note
import com.example.mydatabase.domain.note.NoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSqlDelightNotDataSource: NoteDataSource {
    val nodeList = mutableListOf<Note>()
    override suspend fun insertNode(note: Note) {
        nodeList.add(note)
    }

    override suspend fun getNodeById(id: Long): Note? {
        return nodeList.find { it.id == id }

    }

    override suspend fun getAllNotes(): Flow<List<Note>> {
        return flow { emit(nodeList) }
    }

    override suspend fun deleteNoteById(id: Long) {
        val remove = nodeList.first { it.id == id }
        nodeList.remove(remove)
    }

}