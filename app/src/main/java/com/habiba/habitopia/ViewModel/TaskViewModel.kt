package com.habiba.habitopia.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habiba.habitopia.Repository.TaskRepo
import com.habiba.habitopia.DataBase.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TaskViewModel(private val repo: TaskRepo) : ViewModel() {

    private val _tasksLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksLiveData: LiveData<List<TaskEntity>> get() = _tasksLiveData

    fun getTasksForUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = repo.getTasksForUser(userId)
            _tasksLiveData.postValue(tasks)
        }
    }
    fun insert(taskEntity: TaskEntity)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            repo.insertTask(taskEntity)
        }
    }

    fun markTaskDone(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.markTaskDone(taskId)
        }
    }
    fun toggleTaskDone(taskId: String, done: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setTaskDone(taskId, done)
        }
    }

}
