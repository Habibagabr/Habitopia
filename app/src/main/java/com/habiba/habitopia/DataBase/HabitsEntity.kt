package com.habiba.habitopia.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits_table")
data class HabitsEntity(
    @PrimaryKey(autoGenerate = true)
    val habitId:Int=0,
    val userId:String,
    val habitsTitle:String,
    val habitsEmoji:String,
    val habitsTime:String,
    val habitDone:Int=0
)


