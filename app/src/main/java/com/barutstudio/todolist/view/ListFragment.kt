package com.barutstudio.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.barutstudio.todolist.adapter.TaskAdapter
import com.barutstudio.todolist.databinding.FragmentListBinding
import com.barutstudio.todolist.model.Task
import com.barutstudio.todolist.roomdb.TaskDAO
import com.barutstudio.todolist.roomdb.TaskDB
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val mDisposible = CompositeDisposable()
    private lateinit var db: TaskDB
    private lateinit var taskDao: TaskDAO
    private lateinit var adapter: TaskAdapter
    private var taskList = listOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            TaskDB::class.java, "tasks"
        ).build()

        taskDao = db.taskDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { add(it) }

        adapter = TaskAdapter(taskList, taskDao) { refreshList() }
        binding.taskRecycler.layoutManager = LinearLayoutManager(context)
        binding.taskRecycler.adapter = adapter

        refreshList()

        if (taskList.isEmpty()) {
            binding.willAppearText.visibility = View.VISIBLE
        } else {
            binding.willAppearText.visibility = View.INVISIBLE
        }
    }

    private fun refreshList() {
        mDisposible.add(taskDao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponseForGet))
    }

    private fun handleResponseForGet(taskList: List<Task>) {
        this.taskList = taskList
        adapter.updateTaskList(taskList)
    }

    private fun add(view: View) {
        val action = ListFragmentDirections.actionListFragmentToAddFragment()
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}