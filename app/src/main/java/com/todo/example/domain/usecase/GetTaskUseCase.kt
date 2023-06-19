package com.todo.example.domain.usecase

import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.domain.repository.TaskRepository
import com.todo.example.ui.dashboard.callback.DatabaseOperationInterface
import com.todo.example.ui.dashboard.viewmodel.MainViewModel
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of
 * [MainViewModel] (which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend fun getTaskList(callBack: DatabaseOperationInterface) =
        taskRepository.getTaskList(callBack)

    suspend fun sortList(isSortByName: Boolean, callBack: DatabaseOperationInterface) =
        taskRepository.sortList(isSortByName, callBack)

    suspend fun deleteTask(item: TaskEntity, callBack: DatabaseOperationInterface) =
        taskRepository.deleteTask(item, callBack)

    suspend fun updateTask(id: Int, mStatus: String) = taskRepository.updateTask(id, mStatus)
    suspend fun addTask(item: TaskEntity, callBack: DatabaseOperationInterface) =
        taskRepository.addTask(item, callBack)
}
