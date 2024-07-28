package com.barutstudio.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (

    @ColumnInfo(name = "task_name")
    var taskName : String,

    @ColumnInfo(name = "task_description")
    var taskDescription : String,

    @ColumnInfo(name = "isCompleted")
    var isCompleted : Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}