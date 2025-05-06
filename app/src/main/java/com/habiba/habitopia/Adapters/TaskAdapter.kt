package com.habiba.habitopia.Adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.R
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(private val onTaskClick: (TaskItem.Task) -> Unit) :
    ListAdapter<TaskItem, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    private val taskColors = listOf(
        "#F28585",
        "#27667B",
        "#F28B66",
        "#DEBB90",
    )

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TASK = 1

        // Track last click time for each position to detect double click
        private val lastClickTimeMap = mutableMapOf<Int, Long>()
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
                (holder as TaskViewHolder).bind(item, color, onTaskClick)
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.headerTitle)

        fun bind(item: TaskItem.Header) {
            headerText.text = formatToHumanDate(item.date)
        }

        private fun formatToHumanDate(date: String): CharSequence? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(date)
            val outputFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            return outputFormat.format(date!!)

        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskCard: CardView = itemView.findViewById(R.id.taskCard)
        private val category: TextView = itemView.findViewById(R.id.taskCategory)
        private val taskTime: TextView = itemView.findViewById(R.id.taskTime)
        private val taskIcon: CardView = itemView.findViewById(R.id.checkIcon)

        fun bind(item: TaskItem.Task, color: Int, onClick: (TaskItem.Task) -> Unit) {
            taskTitle.text = item.title
            taskDescription.text = item.description ?: ""
            category.text = item.category
            taskTime.text = "${item.startTime} - ${item.endTime}"

            fun updateUI(done: Boolean) {
                if (done) {
                    taskCard.alpha = 0.4f
                    taskTitle.paintFlags = taskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    taskDescription.alpha = 0.4f
                    taskTime.alpha = 0.4f
                    category.alpha = 0.4f
                    taskIcon.setCardBackgroundColor(color)
                } else {
                    taskCard.alpha = 1f
                    taskTitle.paintFlags = taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    taskDescription.alpha = 1f
                    taskTime.alpha = 1f
                    category.alpha = 1f
                    taskIcon.setCardBackgroundColor(Color.WHITE)

                }
            }

            // Initial UI update
            updateUI(item.taskDone == 1)
            taskCard.setCardBackgroundColor(color)

            // Handle double click
            itemView.setOnClickListener {
                val position = adapterPosition
                val currentTime = System.currentTimeMillis()
                val lastClickTime = lastClickTimeMap[position] ?: 0L

                if (currentTime - lastClickTime < 300) {
                    val toggledTask = item.copy(taskDone = if (item.taskDone == 1) 0 else 1)
                    onClick(toggledTask)  // Update in DB + ViewModel
                    updateUI(toggledTask.taskDone == 1)  // Optimistic UI update
                }

                lastClickTimeMap[position] = currentTime
            }
        }


    }
}
