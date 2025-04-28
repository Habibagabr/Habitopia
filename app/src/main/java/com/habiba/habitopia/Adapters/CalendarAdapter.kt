package com.habiba.habitopia.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.R

class CalendarAdapter(
    private val days: List<DayModel>,
    private val onDayClick: (DayModel) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = days.size

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayName: TextView = itemView.findViewById(R.id.dayName)
        private val dayNumber: TextView = itemView.findViewById(R.id.dayNumber)
        private val card:CardView=itemView.findViewById(R.id.calendercard)

        fun bind(day: DayModel, isSelected: Boolean) {
            dayName.text = day.dayName
            dayNumber.text = day.dayNumber.toString()

            if (isSelected) {
                dayNumber.setBackgroundResource(R.drawable.selectedcircle)
                card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                dayName.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                dayNumber.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            } else {
                dayNumber.setBackgroundResource(R.drawable.circle)
                card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green))
                dayName.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                dayNumber.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            }

            // خلي الكارد ثابت لونه أزرق دايمًا (مش بيتغير مع الضغط)


            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onDayClick(day)
            }
        }
    }
}
