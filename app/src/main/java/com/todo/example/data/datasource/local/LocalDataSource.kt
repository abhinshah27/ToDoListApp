package com.todo.example.data.datasource.local

import com.todo.example.data.datasource.local.dao.TaskDao
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.ui.dashboard.callback.DatabaseOperationInterface

/**
 * it is used for do different type of database operation with help of [TaskDao]
 */
class LocalDataSource constructor(private val taskDao: TaskDao) {
    private val labelSuccess = "Success"
    fun getAllTask(callBack: DatabaseOperationInterface) {
        callBack.getList(taskDao.getAllTask())
    }

    fun sortTask(isSortByName: Boolean, callBack: DatabaseOperationInterface) {
        if (isSortByName) {
            callBack.getList(taskDao.sortByNameTask())
        } else {
            callBack.getList(taskDao.sortByDateTask())
        }
    }

    suspend fun deleteTask(entity: TaskEntity, callBack: DatabaseOperationInterface) {
        taskDao.deleteTask(entity)
        callBack.deleteTask(labelSuccess)
    }

    suspend fun updateTask(id: Int, mStatus: String) = taskDao.updateTask(id, mStatus)
    suspend fun insertTask(entity: TaskEntity, callBack: DatabaseOperationInterface) {
        taskDao.insertTask(entity)
        callBack.addTask(labelSuccess)
    }
}
