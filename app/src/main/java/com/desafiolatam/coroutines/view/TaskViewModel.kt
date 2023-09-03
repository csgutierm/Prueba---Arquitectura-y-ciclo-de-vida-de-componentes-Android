package com.desafiolatam.coroutines.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desafiolatam.coroutines.data.TaskEntity
import com.desafiolatam.coroutines.repository.TaskRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepositoryImp
) : ViewModel() {

    private val _data:
            MutableStateFlow<List<TaskEntity>> = MutableStateFlow(emptyList())
    val taskListStateFlow: StateFlow<List<TaskEntity>> = _data.asStateFlow()

    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO

    init {
        viewModelScope.launch(dispatcherIO) {
            repository.getTasks().collectLatest {
                _data.value = it
            }
        }
    }

    suspend fun deleteTask(task: TaskEntity){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    suspend fun addTask(task: TaskEntity){
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun updateTaskCompleted(taskID: Int, b: Boolean) {
        viewModelScope.launch {
            repository.updateTaskCompleted(taskID, b)
        }

    }
}