package com.example.mydatabase.data.note

import com.example.mydatabase.domain.note.Note
import com.example.mydatabase.domain.note.NoteDataSource
import com.example.mydatabase.domain.time.DateTimeUtil
import com.example.mydatabase.sqldelight.database.NoteDatabase


class SqlDelightNoteDataSource(db: NoteDatabase) : NoteDataSource {
    val queries = db.nodeQueries
    override suspend fun insertNode(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorHex = note.colorHex,
            created = note?.created?.let { DateTimeUtil.toEpochMillis(it) } ?: 0L
        )
    }

    override suspend fun getNodeById(id: Long): Note? {
        return queries.getNoteById(id).executeAsOneOrNull()?.toNote()
    }

    override suspend fun getAllNotes(): List<Note> {
       return queries.getAllNotes().executeAsList().map { it.toNote() }
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteBYId(id)
    }
}