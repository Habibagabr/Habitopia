package com.habiba.habitopia.Repository

import com.habiba.habitopia.DataBase.HabitsDAO
import com.habiba.habitopia.DataBase.HabitsEntity

class HabitsRepo(private val habitDao: HabitsDAO){
    suspend fun insertHabit(habit: HabitsEntity) = habitDao.insertHabit(habit)

    suspend fun deleteHabit(habit: HabitsEntity) = habitDao.deleteHabit(habit)

    suspend fun getHabitsForUser(userId: String): List<HabitsEntity> =
        habitDao.getHabitsForUser(userId)

    suspend fun setHabitDone(habitId: Int, done: Int) =
        habitDao.setHabitDone(habitId, done)
}