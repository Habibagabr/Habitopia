package com.habiba.habitopia.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.habiba.habitopia.HabitsDataClass
import com.habiba.habitopia.R

class habitsAdapter(private val habits: List<HabitsDataClass>):RecyclerView.Adapter<habitsAdapter.habitsViewHolder>() {
    class habitsViewHolder(view: View) : ViewHolder(view) {
        var emojyImage: TextView = view.findViewById(R.id.habitEmoji)
        var habitTitle: TextView = view.findViewById(R.id.habitTitle)
        private var clickImage: ImageView = view.findViewById(R.id.clickCircle)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): habitsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dailyhabitsitem, parent, false)
        return habitsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(holder: habitsViewHolder, position: Int) {
        val habit = habits[position]
        holder.habitTitle.text = habit.taskTitle
        holder.emojyImage.text = habit.taskEmoji
    }
}
