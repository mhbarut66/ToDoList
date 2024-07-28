package com.barutstudio.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barutstudio.todolist.databinding.RecyclerRowBinding
import com.barutstudio.todolist.model.Task
import com.barutstudio.todolist.roomdb.TaskDAO
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskAdapter(
    var taskList: List<Task>,
    private val taskDao: TaskDAO,
    private val refreshList: () -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val mDisposible = CompositeDisposable()

    class TaskViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.binding.titleText.text = task.taskName
        holder.binding.descriptionText.text = task.taskDescription
        holder.binding.isCompleted.isChecked = task.isCompleted

        holder.binding.deleteButton.setOnClickListener {
            deleteTask(task)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateTaskList(newTaskList: List<Task>) {
        taskList = newTaskList
        notifyDataSetChanged()
    }

    private fun deleteTask(task: Task) {
        mDisposible.add(taskDao.delete(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                refreshList()
            })
    }
}