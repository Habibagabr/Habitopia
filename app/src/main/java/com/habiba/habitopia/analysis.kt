package com.habiba.habitopia

import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
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
import com.habiba.habitopia.DataBase.AppDatabase
import com.habiba.habitopia.DataBase.TaskEntity
import com.habiba.habitopia.Repository.TaskRepo
import com.habiba.habitopia.ViewModel.TaskViewModel
import com.habiba.habitopia.ViewModel.TaskViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class analysis : Fragment() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var userId: String


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

        val sharedPref = requireContext().getSharedPreferences("MasterPreference", Context.MODE_PRIVATE)
        userId = sharedPref.getString("userId", "") ?: ""


        val today = LocalDate.now().toString() //  "2025-05-10"
        val todayVal = LocalDate.now()

        val dao = AppDatabase.getDatabase(requireContext()).taskDao()
        val repo = TaskRepo(dao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]

        taskViewModel.getTasksForUser(userId)
        taskViewModel.tasksLiveData.observe(viewLifecycleOwner) { tasksList ->
            val todayTasks = tasksList.filter { it.taskDate == today }
            val completedToday = todayTasks.count { it.taskDone == 1 }.toFloat()
            val incompleteToday = todayTasks.count { it.taskDone == 0 }.toFloat()


            setupPieChart(view, completedToday, incompleteToday)
            setupBarChart(view, tasksList)
            // Summary Texts
            val monthName = todayVal.format(DateTimeFormatter.ofPattern("MMMM"))

            view.findViewById<TextView>(R.id.summaryMonthText).text =monthName
            val tasksThisMonth = tasksList.filter {
                try {
                    val taskDate = LocalDate.parse(it.taskDate)
                    taskDate.year == todayVal.year && taskDate.monthValue == todayVal.monthValue
                } catch (e: Exception) {
                    false
                }
            }

            view.findViewById<TextView>(R.id.summaryTotalText).text = "Total Tasks: ${tasksThisMonth.size}"

        }


    }

    private fun setupBarChart(view: View, tasksList: List<TaskEntity>) {
        val barChart = view.findViewById<BarChart>(R.id.barChart)

        val today = LocalDate.now()
        val startDate = today
        val endDate = today.plusDays(6) // 7 أيام بالتمام

        val tasksPerDay = MutableList(7) { 0f }

        // 🏷 أسماء الأيام بناءً على التواريخ الحقيقية
        val days = (0..6).map { offset ->
            today.plusDays(offset.toLong()).dayOfWeek.name.take(3).capitalize()
        }

        tasksList.forEach {
            try {
                val taskDate = LocalDate.parse(it.taskDate)
                if (taskDate in startDate..endDate) {
                    val dayOffset = taskDate.toEpochDay() - startDate.toEpochDay()
                    if (dayOffset in 0..6) {
                        tasksPerDay[dayOffset.toInt()] = tasksPerDay[dayOffset.toInt()]+ 1
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val entries = tasksPerDay.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value)
        }

        val barDataSet = BarDataSet(entries, "Tasks").apply {
            color = Color.parseColor("#03A9F4")
            valueTextSize = 12f
            valueTextColor = Color.BLACK
        }

        val barData = BarData(barDataSet)

        barChart.apply {
            data = barData
            xAxis.valueFormatter = IndexAxisValueFormatter(days)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            description.isEnabled = false
            animateY(1000)
            invalidate()
        }

        val topDayIndex = tasksPerDay.indexOf(tasksPerDay.maxOrNull())
        view.findViewById<TextView>(R.id.topDayTextView).text =
            "Weekly Tasks Peak: ${days[topDayIndex]}"
    }


    private fun setupPieChart(view: View, completed: Float, incomplete: Float) {
        // Pie Chart setup
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val pieEntries = listOf(
            PieEntry(completed, "Completed"),
            PieEntry(incomplete, "Incomplete"),
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

    }




}

