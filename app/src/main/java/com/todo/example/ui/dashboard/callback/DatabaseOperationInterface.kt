package com.todo.example.ui.dashboard.callback

import com.todo.example.data.datasource.local.LocalDataSource
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.domain.usecase.GetTaskUseCase

/**
 * To make an interaction between
 * [GetTaskUseCase] &
 * [LocalDataSource]
 * */
interface DatabaseOperationInterface {
    fun addTask(message: String)
    fun deleteTask(message: String)
    fun getList(list: List<TaskEntity>)
}