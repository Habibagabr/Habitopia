package com.habiba.habitopia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepo) : ViewModel() {
    private val _tasksLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksLiveData: LiveData<List<TaskEntity>> = _tasksLiveData

    fun getTasksForUser(userId: String){
        viewModelScope.launch {
            val tasks = repository.getTasksForUser(userId)  // نخلي الريبو يرجع List<TaskEntity> عادي مش LiveData
            _tasksLiveData.postValue(tasks)
        }
    }

    fun insertTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
