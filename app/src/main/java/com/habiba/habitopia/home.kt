package com.habiba.habitopia

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.Adapters.CalendarAdapter
import com.habiba.habitopia.Adapters.DayModel
import com.habiba.habitopia.Adapters.TaskAdapter
import com.habiba.habitopia.Adapters.TaskItem
import com.habiba.habitopia.DataBase.TaskDAO
import com.habiba.habitopia.DataBase.TaskDatabase
import com.habiba.habitopia.DataBase.TaskEntity
import com.habiba.habitopia.Repository.TaskRepo
import com.habiba.habitopia.ViewModel.TaskViewModel
import com.habiba.habitopia.ViewModel.TaskViewModelFactory
import com.habiba.habitopia.ViewModel.homeViewModel
import com.habiba.habitopia.databinding.FragmentHomeBinding
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID

class home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: homeViewModel
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var repo: TaskRepo
    private lateinit var database: TaskDatabase
    private lateinit var dao: TaskDAO
    private lateinit var userId: String
    private var allTasks: List<TaskEntity> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // delete the old one to create a new one for the new user :
        userId = setUserId()

        homeViewModel = homeViewModel()


        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val characterImage = sharedPref.getString("character", null)
        if (characterImage != null) {
            val updatedCharacterImage = homeViewModel.setAvater(characterImage, "Happy", "Default")
            renderSvgToBitmapWithDynamicWebView(
                context = binding.avatarImage.context,
                svgUrl = updatedCharacterImage,
                width = 700,
                height = 700
            ) { bitmap ->
                binding.avatarImage.setImageBitmap(bitmap)
            }
        }

        val name = requireContext().getSharedPreferences("name Preference",Context.MODE_PRIVATE)
        val characterName = name.getString("character name", null)

        binding.subtext.text = "Rise and shine! You and $characterName have things to check off today!"

        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TaskAdapter { toggledTask ->
            taskViewModel.toggleTaskDone(toggledTask.taskId, toggledTask.taskDone)
            taskViewModel.getTasksForUser(userId)
        }



        recyclerView.adapter = adapter

        database = TaskDatabase.getDatabase(requireContext())
        dao = database.taskDao()
        repo = TaskRepo(dao)

        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]

        taskViewModel.getTasksForUser(userId)

        taskViewModel.tasksLiveData.observe(viewLifecycleOwner) { tasksList ->
            allTasks = tasksList
            if(allTasks.isEmpty())
                binding.animationContainer.visibility=View.VISIBLE
            else {
                binding.animationContainer.visibility=View.GONE
                val taskItems = mapTasksToTaskItems(tasksList)
                adapter.submitList(taskItems)
            }
        }


        val calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarView)
        val today = LocalDate.now()
        val daysOfWeek = mutableListOf<DayModel>()

        for (i in 0..6) {
            val date = today.plusDays(i.toLong())
            val dayName = date.dayOfWeek.name.take(3)
            val dayNumber = date.dayOfMonth
            val dayYear=date.year
            val dayMonth=date.month

            daysOfWeek.add(DayModel(dayName, dayNumber,dayMonth,dayYear,date.toString()))
            Log.d("date",date.toString())
        }

        val calendarAdapter = CalendarAdapter(daysOfWeek) { dayClicked ->
            val selectedDate = dayClicked.date
            Log.d("task", "Selected Date: $selectedDate")

            val filteredTasks = allTasks.filter {
                Log.d("task", "Checking task with date: ${it.taskDate}")
                it.taskDate == selectedDate
            }
            if(filteredTasks.isEmpty()) {
                binding.animationContainer.visibility = View.VISIBLE
                adapter.submitList(emptyList())
            }

            else {
                binding.animationContainer.visibility = View.GONE
                val taskItems = mapTasksToTaskItems(filteredTasks)
                adapter.submitList(taskItems)
            }
        }
        calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        calendarRecyclerView.adapter = calendarAdapter


        binding.calendericon.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedSelectedDate = String.format(
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay)

                    Log.d("calendaricon", "Selected Date from Calendar Icon: $formattedSelectedDate")

                    val filteredTasks = allTasks.filter {
                        Log.d("calendaricon", "Checking task with date: ${it.taskDate}")
                        it.taskDate == formattedSelectedDate
                    }

                    if(filteredTasks.isEmpty()) {
                        binding.animationContainer.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
                    }
                    else {
                        binding.animationContainer.visibility = View.GONE

                        val taskItems = mapTasksToTaskItems(filteredTasks)
                        adapter.submitList(taskItems)
                    }
                },
                year, month, day
            )

            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        }
    }


    private fun setUserId(): String {
        val sharedPref = context?.getSharedPreferences("MasterPreference", Context.MODE_PRIVATE)
        var userId = sharedPref?.getString("userId", null)

        if (userId == null) {
            userId = UUID.randomUUID().toString()
            sharedPref?.edit()?.putString("userId", userId)?.apply()
        }

        return userId
    }

    private fun mapTasksToTaskItems(tasks: List<TaskEntity>): List<TaskItem> {
        val taskItems = mutableListOf<TaskItem>()
        val groupedTasks = tasks.groupBy { it.taskDate }

        for ((date, tasksOnThatDay) in groupedTasks) {
            taskItems.add(TaskItem.Header(date))
            for (task in tasksOnThatDay) {
                taskItems.add(
                    TaskItem.Task(
                        taskId = task.taskId.toString(),
                        title = task.taskTitle,
                        description = task.taskDescription,
                        startTime = task.taskStartDuration,
                        endTime = task.taskEndDuration,
                        category = task.taskCategory.toString(),
                        taskDone = task.taskDone
                    )
                )
            }
        }

        return taskItems
    }

}
