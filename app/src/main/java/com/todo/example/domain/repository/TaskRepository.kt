package com.todo.example.domain.repository

import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.data.repository.TaskRepositoryImpl
import com.todo.example.domain.usecase.GetTaskUseCase
import com.todo.example.ui.dashboard.callback.DatabaseOperationInterface

/**
 * To make an interaction between
 * [GetTaskUseCase] &
 * [TaskRepositoryImpl]
 * */
interface TaskRepository {
    suspend fun getTaskList(callBack: DatabaseOperationInterface)
    suspend fun sortList(isSortByName: Boolean, callBack: DatabaseOperationInterface)
    suspend fun deleteTask(item: TaskEntity, callBack: DatabaseOperationInterface)
    suspend fun updateTask(id: Int, mStatus: String)
    suspend fun addTask(item: TaskEntity, callBack: DatabaseOperationInterface)
}
