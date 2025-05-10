package com.habiba.habitopia.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.DataBase.HabitsEntity
import com.habiba.habitopia.R

class habitsAdapter(private var habitsList: List<HabitsEntity>) : RecyclerView.Adapter<habitsAdapter.habitsViewHolder>() {

    class habitsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emojiText: TextView = view.findViewById(R.id.habitEmoji)
        val habitTitle: TextView = view.findViewById(R.id.habitTitle)
        val clickImage: ImageView = view.findViewById(R.id.clickCircle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): habitsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dailyhabitsitem, parent, false)
        return habitsViewHolder(view)
    }

    override fun onBindViewHolder(holder: habitsViewHolder, position: Int) {
        val habit = habitsList[position]
        holder.habitTitle.text = habit.habitsTitle
        holder.emojiText.text = habit.habitsEmoji
    }

    override fun getItemCount(): Int {
        return habitsList.size
    }

    fun updateData(newList: List<HabitsEntity>) {
        habitsList = newList
        notifyDataSetChanged()
    }
}
