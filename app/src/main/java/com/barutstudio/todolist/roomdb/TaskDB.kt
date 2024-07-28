package com.barutstudio.todolist.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.barutstudio.todolist.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDB : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
}