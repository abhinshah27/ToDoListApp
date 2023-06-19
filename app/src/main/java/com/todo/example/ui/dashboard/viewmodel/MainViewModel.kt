package com.todo.example.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.example.data.common.HandleResult
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.domain.usecase.GetTaskUseCase
import com.todo.example.ui.dashboard.callback.DatabaseOperationInterface
import com.todo.example.utils.getDateToTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A helper class for the UI controller that is responsible for
 * preparing data for [MainViewModel] as the UI
 */
@HiltViewModel
class MainViewModel @Inject constructor(private var getTaskUseCase: GetTaskUseCase) : ViewModel() {

    private val _resultTask = Channel<HandleResult<List<TaskEntity>>>(Channel.BUFFERED)
    val resultTask: Flow<HandleResult<List<TaskEntity>>> = _resultTask.receiveAsFlow()

    private val _resultAddTask = Channel<HandleResult<String>>(Channel.BUFFERED)
    val resultAddTask: Flow<HandleResult<String>> = _resultAddTask.receiveAsFlow()

    private val _resultDeleteTask = Channel<HandleResult<String>>(Channel.BUFFERED)
    val resultDeleteTask: Flow<HandleResult<String>> = _resultDeleteTask.receiveAsFlow()

    var timeList = mutableListOf("AM", "PM")
    var meridiem = ""
    var time = ""

    fun getTaskList() = viewModelScope.launch(Dispatchers.IO) {
        _resultTask.trySend(HandleResult.Loading)
        getTaskUseCase.getTaskList(object : DatabaseOperationInterface {
            override fun addTask(message: String) {
            }

            override fun deleteTask(message: String) {
            }

            override fun getList(list: List<TaskEntity>) {
                _resultTask.trySend(HandleResult.Success(list))
            }
        })
    }

    fun addTaskList(title: String, time: String, status: String) = viewModelScope.launch {
        _resultAddTask.trySend(HandleResult.Loading)
        getTaskUseCase.addTask(
            TaskEntity(title = title, time = time, status = status, date = getDateToTimestamp(time)),
            object :
                DatabaseOperationInterface {
                override fun addTask(message: String) {
                    _resultAddTask.trySend(HandleResult.Success(message))
                }

                override fun deleteTask(message: String) {
                }

                override fun getList(list: List<TaskEntity>) {
                }
            },
        )
    }

    fun deleteTask(taskIem: TaskEntity) = viewModelScope.launch {
        _resultDeleteTask.trySend(HandleResult.Loading)
        getTaskUseCase.deleteTask(
            taskIem,
            object :
                DatabaseOperationInterface {
                override fun addTask(message: String) {
                }

                override fun deleteTask(message: String) {
                    _resultDeleteTask.trySend(HandleResult.Success(message))
                    getTaskList()
                }

                override fun getList(list: List<TaskEntity>) {
                }
            },
        )
    }

    fun updateTask(id: Int, mStatus: String) = viewModelScope.launch {
        getTaskUseCase.updateTask(id, mStatus)
        getTaskList()
    }

    fun sortTask(isSortByName: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _resultTask.trySend(HandleResult.Loading)
        getTaskUseCase.sortList(
            isSortByName,
            object : DatabaseOperationInterface {
                override fun addTask(message: String) {
                }

                override fun deleteTask(message: String) {
                }

                override fun getList(list: List<TaskEntity>) {
                    _resultTask.trySend(HandleResult.Success(list))
                }
            },
        )
    }
}
