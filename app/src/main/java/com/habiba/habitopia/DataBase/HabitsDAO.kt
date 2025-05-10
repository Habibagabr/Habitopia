package com.habiba.habitopia.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitsEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitsEntity)

    @Query("SELECT * FROM habits_table WHERE userId = :userId")
    suspend fun getHabitsForUser(userId: String): List<HabitsEntity>

    @Query("UPDATE habits_table SET habitDone = 1 WHERE habitId = :habitId")
    suspend fun markHabitAsDone(habitId: Int)

    @Query("UPDATE habits_table SET habitDone = :done WHERE habitId = :habitId")
    suspend fun setHabitDone(habitId: Int, done: Int)
}
