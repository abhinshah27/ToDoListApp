package com.todo.example.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.todo.example.data.datasource.local.dao.TaskDao
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.utils.Constants.TO_DO_DATABASE

/**
 * To manage data items that can be accessed, updated
 * & maintain relationships between them
 */
@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun dao(): TaskDao

    companion object {
        @Volatile
        private var instance: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(appContext: Context) = Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            TO_DO_DATABASE,
        ).fallbackToDestructiveMigration().build()
    }
}
