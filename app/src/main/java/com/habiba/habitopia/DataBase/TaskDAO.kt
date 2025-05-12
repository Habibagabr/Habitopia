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

    @Query("DELETE FROM task_table WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("SELECT * FROM task_table WHERE userId = :userId ORDER BY taskDate ")
    suspend fun getTasksForUser(userId: String):List<TaskEntity>


    @Query("UPDATE task_table SET taskDone = :done WHERE taskId = :taskId")
    suspend fun setTaskDone(taskId: Int, done: Int)

}