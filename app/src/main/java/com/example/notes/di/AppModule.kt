package com.example.notes.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import com.example.mydatabase.data.local.DatabaseDriverFactory
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
object AppModule {

    @Provides
    @Singleton
    fun providesSqlDriver(app: Application): SqlDriver{
        return DatabaseDriverFactory(app).createDriver()
    }

    @Provides
    @Singleton
    fun providesNodeDataSource(driver: SqlDriver): NoteDataSource{
        return SqlDelightNoteDataSource(NoteDatabase(driver))
    }
}