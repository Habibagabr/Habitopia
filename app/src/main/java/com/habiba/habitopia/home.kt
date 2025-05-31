package com.habiba.habitopia

import HabitViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.habiba.habitopia.Adapters.*
import com.habiba.habitopia.DataBase.*
import com.habiba.habitopia.Repository.*
import com.habiba.habitopia.ViewModel.*
import com.habiba.habitopia.databinding.FragmentHomeBinding
import com.habiba.habitopia.helperFunction.insertHabitsFromPreferences
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView
import java.time.LocalDate
import java.util.*

class home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var habitsViewModel: HabitViewModel
    private lateinit var repo: TaskRepo
    private lateinit var database: AppDatabase
    private lateinit var userId: String
    private lateinit var habitsAdapter: habitsAdapter
    private lateinit var habitsRecyclerView: RecyclerView
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

        val firestore = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()


        homeViewModel = HomeViewModel()
        database = AppDatabase.getDatabase(requireContext())

        // Initialize ViewModels
        repo = TaskRepo(database.taskDao())
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]

        val habitsRepo = HabitsRepo(database.habitsDao())
        habitsViewModel = ViewModelProvider(this, HabitViewModelFactory(habitsRepo))[HabitViewModel::class.java]

        //  Insert habits from SharedPreferences to Room
        insertHabitsFromPreferences(requireContext(), userId) { habit ->
            habitsViewModel.insertHabit(habit)
        }

        // Character Avatar Rendering
        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPref.getString("character", null)?.let { characterImage ->
            val updatedCharacterImage = homeViewModel.setAvater(characterImage, "Happy", "Default")
            renderSvgToBitmapWithDynamicWebView(
                context = binding.avatarImage.context,
                svgUrl = updatedCharacterImage,
                width = 700,
                height = 700
            ) { bitmap -> binding.avatarImage.setImageBitmap(bitmap) }
        }
        //top text


        if (userId != null) {
            firestore.collection("UserData")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("UserName")
                        val characterURL = document.getString("characterURL")

                        // هنا استخدمهم في الـ UI
                        binding.greetingText.text = "Good Morning, $username!"
                    } else {
                        binding.subtext.text = "Welcome!"
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        }



        // Greeting text
        val namePref = requireContext().getSharedPreferences("name Preference", Context.MODE_PRIVATE)
        val characterName = namePref.getString("character name", null)
        binding.subtext.text = "Rise and shine! You and $characterName have things to check off today!"

        // Tasks RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TaskAdapter(
            onTaskClick = {
                taskViewModel.toggleTaskDone(it.taskId, it.taskDone)
                if (userId != null) {
                    taskViewModel.getTasksForUser(userId)
                }
            },
            onTaskDelete = { task ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        taskViewModel.deleteTask(task.taskId)
                        Toast.makeText(requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
                        if (userId != null) {
                            taskViewModel.getTasksForUser(userId)
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        recyclerView.adapter = adapter

        // Load tasks
        if (userId != null) {
            taskViewModel.getTasksForUser(userId)
        }
        taskViewModel.tasksLiveData.observe(viewLifecycleOwner) { tasksList ->
            allTasks = tasksList
            val taskItems = mapTasksToTaskItems(tasksList)
            binding.animationContainer.visibility = if (taskItems.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(taskItems)
        }

        // Calendar setup
        val calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarView)
        val today = LocalDate.now()
        val daysOfWeek = (0..6).map {
            val date = today.plusDays(it.toLong())
            DayModel(date.dayOfWeek.name.take(3), date.dayOfMonth, date.month, date.year, date.toString())
        }
        val calendarAdapter = CalendarAdapter(daysOfWeek) { selected ->
            val filtered = allTasks.filter { it.taskDate == selected.date }
            binding.animationContainer.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(mapTasksToTaskItems(filtered))
        }
        calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        calendarRecyclerView.adapter = calendarAdapter

        // Calendar icon date picker
        binding.calendericon.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                val filtered = allTasks.filter { it.taskDate == selectedDate }
                binding.animationContainer.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
                adapter.submitList(mapTasksToTaskItems(filtered))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .apply { datePicker.minDate = calendar.timeInMillis }
                .show()
        }

        // Habits RecyclerView
        habitsAdapter = habitsAdapter(emptyList())
        habitsRecyclerView = view.findViewById(R.id.habitsRecyclerView)
        habitsRecyclerView.adapter = habitsAdapter
        habitsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        if (userId != null) {
            habitsViewModel.getHabitsForUser(userId)
        }
        habitsViewModel.habitsLiveData.observe(viewLifecycleOwner) {
            habitsAdapter.updateData(it)
        }

        // Add new habit manually
        binding.addnewHabit.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
            val taskNameInput = dialogView.findViewById<EditText>(R.id.taskNameInput)
            val emojiInput = dialogView.findViewById<EditText>(R.id.emojiInput)
            val timeText = dialogView.findViewById<TextView>(R.id.timeText)

            var selectedHour = 0
            var selectedMinute = 0

            timeText.setOnClickListener {
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    selectedHour = hour
                    selectedMinute = minute
                    timeText.text = String.format("%02d:%02d", hour, minute)
                }, 12, 0, true).show()
            }

            val titleView = LayoutInflater.from(requireContext()).inflate(R.layout.newhabittitle, null)

            AlertDialog.Builder(requireContext())
                .setCustomTitle(titleView)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val newHabit = userId?.let { it1 ->
                        HabitsEntity(
                            userId = it1,
                            habitsTitle = taskNameInput.text.toString(),
                            habitsEmoji = emojiInput.text.toString(),
                            habitsTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                        )
                    }
                    if (newHabit != null) {
                        habitsViewModel.insertHabit(newHabit)
                    }
                    Toast.makeText(requireContext(), "The new Habit Added!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .create().apply {
                    show()
                    window?.setBackgroundDrawableResource(R.drawable.rounded_button)
                    getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    getButton(AlertDialog.BUTTON_POSITIVE)?.textSize = 16f
                    getButton(AlertDialog.BUTTON_NEGATIVE)?.textSize = 16f
                }
        }
    }


    private fun mapTasksToTaskItems(tasks: List<TaskEntity>): List<TaskItem> {
        val groupedTasks = tasks.groupBy { it.taskDate }
        val taskItems = mutableListOf<TaskItem>()
        for ((date, dayTasks) in groupedTasks) {
            taskItems.add(TaskItem.Header(date))
            dayTasks.forEach { task ->
                taskItems.add(
                    TaskItem.Task(
                        taskId = task.taskId,
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
