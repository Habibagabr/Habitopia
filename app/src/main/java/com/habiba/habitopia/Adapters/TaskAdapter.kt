package com.habiba.habitopia.Adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.R


/*
* point of concern :
* recycler view have to read from the live data in the view model , we will use a new approach for me which is :
* we won't build a new recycler view content every time new task added as its not efficient and will lead to lag specially when the list is too huge after time
* so we will use a new approach which is depends on comparing between the old and the new list , update which is only need for update
* so the adapter will inherit from ListAdapter and we will create the class which is responsible for the comparing
* in the past --> inherit from recycler view adapter --> takes list<TaskItem> every time take the list as a new list neglect the past one
* now --> inherit from listAdapter "improved version of recyclerView.adapter"
* when to use the default one ? --> no a lot of changes happened to the recycler view " like colour recycler view "
* when to use the new one ? --> a lot of changes made " user adding , removing , editing "
* */
class TaskAdapter : ListAdapter<TaskItem, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    private val taskColors = listOf(
        "#F28585",
        "#27667B",
        "#F28B66",
        "#DEBB90",
    )

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TASK = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TaskItem.Header -> VIEW_TYPE_HEADER
            is TaskItem.Task -> VIEW_TYPE_TASK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.taskheader, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_TASK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.taskitem, parent, false)
                TaskViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TaskItem.Header -> (holder as HeaderViewHolder).bind(item)
            is TaskItem.Task -> {
                val color = Color.parseColor(taskColors[position % taskColors.size])
                (holder as TaskViewHolder).bind(item, color)
                Log.d("color first", taskColors[position % taskColors.size])
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.headerTitle)

        fun bind(item: TaskItem.Header) {
            headerText.text = item.date
        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskCard: CardView = itemView.findViewById(R.id.taskCard)

        fun bind(item: TaskItem.Task, color: Int) {
            taskTitle.text = item.title
            taskDescription.text = item.description
            taskCard.setCardBackgroundColor(color)
            Log.d("color", color.toString())
        }
    }
}

