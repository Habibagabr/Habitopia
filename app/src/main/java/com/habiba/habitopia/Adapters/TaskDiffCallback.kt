package com.habiba.habitopia.Adapters

import androidx.recyclerview.widget.DiffUtil

// * this is the class which is responsible for the comparison between the two new lists --> " internally efficient for loop "
// *class inherit from DiffUtil --> abstract class take the lists needed to be compared we just passing the data type of the lists
// * override the functions
// * areItemsTheSame --> comparing each item in the two lists : --عشان نعرف نمسك الايتيم الي في الليست التانيه الي شبهك لكن كداا لسه معملناش مقارنه في المحتوي كامل
// *              - if both of them is header --> compare by the data
// *              - if both of them is task    --> compare by the title
// *               - else ? definitely not the same item
// * areContentsTheSame --> now we have two similar headers or tasks in date or title , lets compare the whole inside content and return boolean


class TaskDiffCallback : DiffUtil.ItemCallback<TaskItem>() {
    override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
        return when {
            oldItem is TaskItem.Header && newItem is TaskItem.Header -> oldItem.date == newItem.date
            oldItem is TaskItem.Task && newItem is TaskItem.Task -> oldItem.title == newItem.title
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
        return oldItem == newItem
    }
}


