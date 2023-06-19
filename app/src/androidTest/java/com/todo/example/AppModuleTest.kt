package com.todo.example

import com.todo.example.data.datasource.local.LocalDataSource
import com.todo.example.data.datasource.local.dao.TaskDao
import com.todo.example.data.repository.TaskRepositoryImpl
import com.todo.example.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * provide different types of data with[dagger.hilt]
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModuleTest {

    @Provides
    @Singleton
    fun provideLocalDataSource(taskDao: TaskDao): LocalDataSource {
        return LocalDataSource(taskDao)
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        localDataSource: LocalDataSource,
    ): TaskRepository {
        return TaskRepositoryImpl(
            localDataSource = localDataSource
        )
    }
}
