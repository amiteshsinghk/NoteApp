package com.example.mydatabase.domain.note

import com.example.mydatabase.domain.time.DateTimeUtil

class SearchNotesUseCase {

    fun execute(notes: List<Note>, query: String): List<Note>{
        if (query.isBlank()){
            return notes
        }
        return notes.filter {
            it.title.trim().lowercase().contains(query.lowercase()) ||
                    it.content.trim().lowercase().contains(query.lowercase())
        }.sortedBy {
            it?.created?.let { it1 -> DateTimeUtil.toEpochMillis(it1) }
        }
    }

}