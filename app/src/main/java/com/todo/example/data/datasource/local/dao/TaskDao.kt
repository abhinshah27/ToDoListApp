package com.todo.example.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.todo.example.data.datasource.local.entity.TaskEntity

/**
 * it provides access to [TaskEntity] underlying database
 * */
@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTask(): List<TaskEntity>

    @Query("SELECT * FROM task order by title COLLATE NOCASE ASC")
    fun sortByNameTask(): List<TaskEntity>

    @Query("SELECT * FROM task order by date ASC")
    fun sortByDateTask(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntities: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntities: TaskEntity)

    @Query("UPDATE task SET status = :mStatus WHERE id = :id")
    suspend fun updateTask(id: Int, mStatus: String)
}
