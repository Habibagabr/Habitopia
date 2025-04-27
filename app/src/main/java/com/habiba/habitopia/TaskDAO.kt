package com.habiba.habitopia

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task:TaskEntity)

    @Delete
    suspend fun deleteTask(task:TaskEntity)

    @Query("SELECT * FROM task_table WHERE userId = :userId ORDER BY userId ASC")
    fun getTasksForUser(userId: String): LiveData<List<TaskEntity>>


}