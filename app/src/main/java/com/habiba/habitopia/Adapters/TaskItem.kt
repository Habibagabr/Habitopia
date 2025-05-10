package com.habiba.habitopia.Adapters

sealed class TaskItem {
        data class Header(val date: String) : TaskItem()
        data class Task(
            val taskId: Int,
            val title: String,
            val description: String?,
            val startTime: String,
            val endTime: String,
            val category: String,
            val taskDone: Int  // 🟢 Required for UI updates
        ) : TaskItem()
    }


