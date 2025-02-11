package com.barutstudio.todolist.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.barutstudio.todolist.model.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TaskDAO {

    @Query("SELECT * FROM Task")
    fun getAll(): Flowable<List<Task>>

    @Query("SELECT * FROM Task WHERE id = :id")
    fun getById(id: Int): Flowable<Task>

    @Insert
    fun insert(task: Task) : Completable

    @Delete
    fun delete(task: Task) : Completable

}