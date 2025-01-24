package com.example.notes.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.mydatabase.data.note.SqlDelightNoteDataSource
import com.example.mydatabase.domain.note.NoteDataSource
import com.example.mydatabase.sqldelight.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun providesSqlDriver(app: Application): SqlDriver{
        return AndroidSqliteDriver(NoteDatabase.Schema, app, null)
    }

    @Provides
    @Singleton
    fun providesNodeDataSource(driver: SqlDriver): NoteDataSource{
        return SqlDelightNoteDataSource(NoteDatabase(driver))
    }
}