package com.habiba.habitopia.Adapters

sealed class TaskItem {
    data class Header(val date: String) : TaskItem()
    data class Task(val title: String, val description: String, val startTime: String, val endTime: String) : TaskItem()
}

