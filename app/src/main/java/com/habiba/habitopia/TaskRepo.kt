package com.habiba.habitopia

import androidx.lifecycle.LiveData

class TaskRepo(private val taskDao:TaskDAO) {

    // Get all tasks for a specific user
    fun getTasksForUser(userId: String): LiveData<List<TaskEntity>> {
        return taskDao.getTasksForUser(userId)
    }

    // Insert a new task
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    // Delete a task
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

}