package com.habiba.habitopia.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM task_table WHERE userId = :userId ORDER BY taskDate ")
    suspend fun getTasksForUser(userId: String):List<TaskEntity>

    @Query("UPDATE task_table SET taskDone = 1 WHERE taskId = :taskId")
    suspend fun markTaskAsDone(taskId: String)
    
    @Query("UPDATE task_table SET taskDone = :done WHERE taskId = :taskId")
    suspend fun setTaskDone(taskId: String, done: Int)

}