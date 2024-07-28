package com.barutstudio.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import com.barutstudio.todolist.R
import com.barutstudio.todolist.databinding.FragmentAddBinding
import com.barutstudio.todolist.model.Task
import com.barutstudio.todolist.roomdb.TaskDAO
import com.barutstudio.todolist.roomdb.TaskDB
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val mDisposible = CompositeDisposable()
    private lateinit var db : TaskDB
    private lateinit var taskDao : TaskDAO

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
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener { add(it) }
    }

    fun add(view: View) {
        val taskName = binding.titleText.text.toString()
        val taskDescription = binding.descriptionText.text.toString()

        val task = Task(taskName, taskDescription, false)

        mDisposible.add(taskDao.insert(task).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe(this::handleResponseForInsert))

    }

    fun handleResponseForInsert() {
        val action = AddFragmentDirections.actionAddFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}