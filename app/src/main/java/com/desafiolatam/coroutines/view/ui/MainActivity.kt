package com.desafiolatam.coroutines.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.desafiolatam.coroutines.R
import com.desafiolatam.coroutines.data.TaskEntity
import com.desafiolatam.coroutines.databinding.ActivityMainBinding
import com.desafiolatam.coroutines.view.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()

    @Inject
    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getTaskList()

        val fabAddTask: FloatingActionButton = findViewById(R.id.fabAddTask)
        fabAddTask.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }

/*        val checkbox = findViewById<CheckBox>(R.id.checkbox)
        checkbox.setOnClickListener {
            checkbox.isChecked = !checkbox.isChecked
        }*/
    }

    private fun getTaskList() {
        lifecycleScope.launchWhenCreated {
            viewModel.taskListStateFlow.collectLatest {
                initRecyclerView(it)
            }
        }
    }

    private fun initRecyclerView(taskList: List<TaskEntity>) {
        adapter = TaskAdapter()
        adapter.taskList = taskList
        binding.rvTask.layoutManager = LinearLayoutManager(this)
        binding.rvTask.adapter = adapter

        adapter.onLongClick = {
            deleteTask(it)
            Toast.makeText(this@MainActivity, "Tarea borrada", Toast.LENGTH_SHORT).show()
        }

/*        adapter.onClick = {
            val task = TaskEntity(
                id = 0, // El valor de "id" se establece en 0 si quieres que se genere automáticamente.
                title = "Título de prueba",
                description = "Descripción de prueba"
            )
            addTask(task)
        }*/

        adapter.onClick = {
            Log.i("CLIC", "Cliqueado")
            updateTaskCompleted(it)
        }
    }

    private fun deleteTask(task: TaskEntity) {
        lifecycleScope.launchWhenCreated {
            viewModel.deleteTask(task)
        }
    }

    private fun addTask(task: TaskEntity) {
        lifecycleScope.launchWhenCreated {
            viewModel.addTask(task)
        }
    }



    private fun updateTaskCompleted(task: TaskEntity) {
        lifecycleScope.launchWhenCreated {
            viewModel.updateTaskCompleted(task.id, !task.completada)
        }
    }


}