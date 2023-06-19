package com.todo.example.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * database table
 */
@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "date") var date: Long?,
)
