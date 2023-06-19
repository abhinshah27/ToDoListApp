package com.todo.example.di

import android.content.Context
import com.todo.example.data.datasource.local.dao.TaskDao
import com.todo.example.data.datasource.local.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * provide different types of data with[dagger.hilt]
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): TaskDatabase =
        TaskDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.dao()
}
