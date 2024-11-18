package com.example.mydatabase.data.note

import app.cash.sqldelight.coroutines.asFlow
import com.example.mydatabase.domain.note.Note
import com.example.mydatabase.domain.note.NoteDataSource
import com.example.mydatabase.domain.time.DateTimeUtil
import com.example.mydatabase.sqldelight.database.NoteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SqlDelightNoteDataSource(db: NoteDatabase) : NoteDataSource {
    private val queries = db.nodeQueries
    override suspend fun insertNode(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorHex = note.colorHex,
            created = DateTimeUtil.toEpochMillis(note.created)
        )
    }

    override suspend fun getNodeById(id: Long): Note? {
        return queries.getNoteById(id).executeAsOneOrNull()?.toNote()
    }

    override suspend fun getAllNotes(): Flow<List<Note>> {
//        return queries.getAllNotes().executeAsList().map { it.toNote() }
       return queries.getAllNotes()
            .asFlow()
            .map { queryResult ->
                queryResult.executeAsList().map { it.toNote() }
            }
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteBYId(id)
    }
}