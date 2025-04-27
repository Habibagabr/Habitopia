package com.habiba.habitopia.Adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.R

class TaskAdapter(private val items: List<TaskItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val taskColors = listOf(
        "#F28585",
        "#27667B",
        "#F28B66",
        "#858C4D",

        )

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TASK = 1
    }


    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
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
        when (val item = items[position]) {
            is TaskItem.Header -> (holder as HeaderViewHolder).bind(item)
            is TaskItem.Task -> {
                val color = Color.parseColor(taskColors[position % taskColors.size])
                (holder as TaskViewHolder).bind(item, color)
                Log.d("color first",taskColors[position % taskColors.size])
            }
        }
    }

    override fun getItemCount() = items.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.headerTitle)

        fun bind(item: TaskItem.Header) {
            headerText.text = item.date
        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescribText)
        private val taskTime: TextView = itemView.findViewById(R.id.taskDuration)
        private val taskTitleCard: CardView = itemView.findViewById(R.id.taskTitlecard)
        private val taskCard: CardView = itemView.findViewById(R.id.taskDescribcard)

        fun bind(item: TaskItem.Task, color: Int) {
            taskTitle.text = item.title
            taskDescription.text = item.description
            taskTime.text = "${item.startTime} - ${item.endTime}"

            taskCard.setCardBackgroundColor(color)
            Log.d("color",color.toString())
            taskTitleCard.setCardBackgroundColor(color)
        }
    }
}
