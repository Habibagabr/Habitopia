package com.habiba.habitopia

import android.content.Context
import android.os.Bundle
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
import com.habiba.habitopia.CharactersData.homeViewModel
import com.habiba.habitopia.databinding.FragmentHomeBinding
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView
import java.time.LocalDate
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val name = homeViewModel.characterName
        binding.subtext.text = "Rise and shine! You and $name have things to check off today!"

        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TaskAdapter()
        recyclerView.adapter = adapter

        database = TaskDatabase.getDatabase(requireContext())
        dao = database.taskDao()
        repo = TaskRepo(dao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]

        taskViewModel.getTasksForUser(userId)
        taskViewModel.tasksLiveData.observe(viewLifecycleOwner) { tasksList ->
            val taskItems = mapTasksToTaskItems(tasksList)
            adapter.submitList(taskItems)
        }

        val calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarView)
        val today = LocalDate.now()
        val daysOfWeek = mutableListOf<DayModel>()

        for (i in 0..6) {
            val date = today.plusDays(i.toLong())
            val dayName = date.dayOfWeek.name.take(3)
            val dayNumber = date.dayOfMonth

            daysOfWeek.add(DayModel(dayName, dayNumber))
        }

        val calendarAdapter = CalendarAdapter(daysOfWeek) { dayClicked ->
            // Handle day clicked if needed
        }

        calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        calendarRecyclerView.adapter = calendarAdapter
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
                        title = task.taskTitle,
                        description = task.taskDescription,
                        startTime = task.taskStartDuration,
                        endTime = task.taskEndDuration
                    )
                )
            }
        }

        return taskItems
    }
}
