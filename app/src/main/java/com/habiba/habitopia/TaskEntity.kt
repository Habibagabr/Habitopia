package com.habiba.habitopia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val userId :Int=0,
    val taskTitle:String,
    val taskDescription:String,
    val taskStartDuration:String,
    val taskEndDuration:String,
    val taskDate:String

)

