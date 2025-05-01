package com.habiba.habitopia.Repository

import com.habiba.habitopia.DataBase.TaskDAO
import com.habiba.habitopia.DataBase.TaskEntity

class TaskRepo(private val taskDao: TaskDAO) {

    // Get all tasks for a specific user
    suspend fun getTasksForUser(userId: String): List<TaskEntity> {

        return taskDao.getTasksForUser(userId)
    }

    // Insert a new task
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    // Delete a task from the database totally
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    // Mark task as done
    suspend fun markTaskDone(taskId: String)
    {
        taskDao.markTaskAsDone(taskId)
    }

    suspend fun setTaskDone(taskId: String, done: Int) {
        taskDao.setTaskDone(taskId, done)
    }


}