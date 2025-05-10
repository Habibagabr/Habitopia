package com.habiba.habitopia

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class analysis : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Fake data
        val completedTasks = 20f
        val totalTasks = 25f
        val incompleteTasks = totalTasks - completedTasks
        val tasksThisMonth = 88
        val tasksLastMonth = 80

        // Pie Chart setup
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val pieEntries = listOf(
            PieEntry(completedTasks, "Completed"),
            PieEntry(incompleteTasks, "Incomplete"),
        )
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = listOf(
            Color.parseColor("#4CAF50"), // green
            Color.parseColor("#B0BEC5"),
        )
        pieDataSet.valueTextSize = 14f
        pieDataSet.valueTextColor = Color.WHITE
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.setCenterText("Today")
        pieChart.setCenterTextSize(16f)
        val centerText = SpannableString("Today")
        centerText.setSpan(StyleSpan(Typeface.BOLD), 0, centerText.length, 0)
        pieChart.centerText = centerText
        pieChart.setDrawEntryLabels(false)
        pieChart.setCenterTextColor(Color.parseColor("#DEBB90"))
        pieChart.animateY(1000)
        pieChart.invalidate()

        // Summary Texts
        view.findViewById<TextView>(R.id.summaryMonthText).text = "May"
        view.findViewById<TextView>(R.id.summaryTotalText).text = "Total Tasks: $tasksThisMonth"
        val difference = tasksThisMonth - tasksLastMonth
        // Bar Chart - Tasks per day
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val tasksPerDay = listOf(3f, 5f, 4f, 6f, 2f, 7f, 1f)
        val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val barEntries = tasksPerDay.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value)
        }
        val barDataSet = BarDataSet(barEntries, "Tasks")
        barDataSet.color = Color.parseColor("#03A9F4")
        barDataSet.valueTextSize = 12f
        barDataSet.valueTextColor = Color.BLACK
        val barData = BarData(barDataSet)
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(days)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()

        // Optional: highlight top productive day
        val maxIndex = tasksPerDay.indexOf(tasksPerDay.maxOrNull())
        view.findViewById<TextView>(R.id.topDayTextView).text = "Productivity Peak: ${days[maxIndex]}"
    }





    }

