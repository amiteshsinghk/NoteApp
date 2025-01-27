package com.example.notes.noteList

import androidx.lifecycle.SavedStateHandle
import com.example.mydatabase.domain.note.Note
import com.example.mydatabase.domain.note.NoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoteListViewModelTest{
    private lateinit var viewModel: NoteListViewModel
    private lateinit var noteDataSource: NoteDataSource
    private lateinit var savedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        noteDataSource = FakeSqlDelightNotDataSource()
        savedStateHandle = SavedStateHandle(
            mapOf(
                "notes" to emptyList<Note>(),
                "searchText" to "",
                "isSearchActive" to false
            )
        )

        viewModel = NoteListViewModel(noteDataSource, savedStateHandle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }


    @Test
    fun `loadNotes should update state with fetched notes`()=  runBlocking{
        getFakeNotes.forEachIndexed { index, note ->
            noteDataSource.insertNode(note)
        }

        viewModel.loadNotes()
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedNotes = savedStateHandle.get<List<Note>>("notes")
        assertEquals(getFakeNotes, updatedNotes)
    }

    @Test
    fun `search text changes should update state`(){
        val searchNewText = "new Text"
        viewModel.onSearchTextChange(searchNewText)
        assertEquals(searchNewText, savedStateHandle.get("searchText"))

    }

    @Test
    fun `toggle search should update state`(){
        viewModel.onToggleSearch()
        assertEquals(true, savedStateHandle.get("isSearchActive"))
    }

    @Test
    fun `delete note by id should call data source and update state`() = runBlocking{
       getFakeNotes.forEachIndexed { index, note ->
           noteDataSource.insertNode(note)
       }
        viewModel.loadNotes()
        viewModel.deleteNoteById(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        val updatedNotes = savedStateHandle.get<List<Note>>("notes")
        assertEquals(1, updatedNotes?.size)
    }

    val fixedDateTime = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val getFakeNotes = listOf(
        Note(1L, "Note 1", "Content 1", 100L, fixedDateTime),
        Note(2L, "Note 2", "Content 2", 200L, fixedDateTime)
    )

}