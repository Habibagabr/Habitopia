package com.habiba.habitopia.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId :Int=0,
    val userId:String,
    var taskTitle:String,
    var taskDescription:String,
    var taskStartDuration:String,
    var taskEndDuration:String,
    var taskDate:String,
    var taskCategory:String?,
    var taskDone:Int=0

)

