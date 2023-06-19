package com.todo.example.data.repository

import com.todo.example.data.datasource.local.LocalDataSource
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.domain.repository.TaskRepository
import com.todo.example.ui.dashboard.callback.DatabaseOperationInterface

/**
 * This repository is responsible for
 * fetching data[TaskEntity] from server or db
 * */
class TaskRepositoryImpl(
    private val localDataSource: LocalDataSource,
) : TaskRepository {
    override suspend fun getTaskList(callBack: DatabaseOperationInterface) {
        localDataSource.getAllTask(callBack)
    }

    override suspend fun sortList(isSortByName: Boolean, callBack: DatabaseOperationInterface) {
        localDataSource.sortTask(isSortByName, callBack)
    }

    override suspend fun addTask(item: TaskEntity, callBack: DatabaseOperationInterface) {
        localDataSource.insertTask(item, callBack)
    }

    override suspend fun deleteTask(item: TaskEntity, callBack: DatabaseOperationInterface) {
        localDataSource.deleteTask(item, callBack)
    }

    override suspend fun updateTask(id: Int, mStatus: String) {
        localDataSource.updateTask(id, mStatus)
    }
}
