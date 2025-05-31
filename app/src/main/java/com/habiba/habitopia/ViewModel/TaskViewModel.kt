package com.habiba.habitopia.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habiba.habitopia.Repository.TaskRepo
import com.habiba.habitopia.DataBase.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TaskViewModel(private val repo: TaskRepo) : ViewModel() {

    private val _tasksLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksLiveData: LiveData<List<TaskEntity>> get() = _tasksLiveData

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName


    private var currentUserId: String = ""

    fun getTasksForUser(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
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

    fun toggleTaskDone(taskId: Int, done: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setTaskDone(taskId, done)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repo.deleteTask(taskId)
            getTasksForUser(currentUserId)
        }
    }


    fun fetchUserName(userId: String) {
        viewModelScope.launch {
            val name = repo.getUserNameForUser(userId)
            _userName.postValue(name ?: "Unknown User")
        }
    }






}
