package com.habiba.habitopia

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.habiba.habitopia.DataBase.AppDatabase
import com.habiba.habitopia.DataBase.TaskDAO
import com.habiba.habitopia.DataBase.TaskEntity
import com.habiba.habitopia.Repository.TaskRepo
import com.habiba.habitopia.ViewModel.TaskViewModel
import com.habiba.habitopia.ViewModel.TaskViewModelFactory
import com.habiba.habitopia.databinding.FragmentAddtaskBinding
import java.util.Calendar

class addtask : Fragment() {
    private var _binding:FragmentAddtaskBinding?=null
    private val binding  get() = _binding!!
    private lateinit var viewModel: TaskViewModel
    private lateinit var repo: TaskRepo
    private lateinit var database: AppDatabase
    private lateinit var dao: TaskDAO
    private var taskEntity: TaskEntity?=null
    private lateinit var userId:String
    private var category:String?="Your Task"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentAddtaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestore = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()


        database = AppDatabase.getDatabase(requireContext())
        dao = database.taskDao()
        repo = TaskRepo(dao)
        viewModel = ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]


        binding.startTimeButton.setOnClickListener {
            setTime(binding.startTimeButton)
        }
        binding.endTimeButton.setOnClickListener {
            setTime(binding.endTimeButton)
        }

        binding.datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    binding.datePickerButton.text = formattedDate
                    updateDoneButtonColor()
                },
                year, month, day
            )
            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        }

        binding.editText.addTextChangedListener {
            updateDoneButtonColor()
        }

        binding.Done.setOnClickListener {
            checkVerifyingNullInput()?.let { task -> viewModel.insert(task) }
        }
        binding.chipPersonalGrowth.setOnClickListener{
            setClickedChiped(binding.chipPersonalGrowth)
        }
        binding.chipHealthWellness.setOnClickListener{
            setClickedChiped(binding.chipHealthWellness)
        }
        binding.chipWorkProjects.setOnClickListener{
            setClickedChiped(binding.chipWorkProjects)
        }
        binding.chipDailyRoutine.setOnClickListener(){
            setClickedChiped(binding.chipDailyRoutine)
        }
        binding.chipProductivity.setOnClickListener{
            setClickedChiped(binding.chipProductivity)
        }
        binding.editTextCategory.setOnClickListener{
            category=binding.editTextCategory.text.toString()
        }
    }

    private fun updateDoneButtonColor() {
        val title = binding.editText.text.toString().trim()
        val date = binding.datePickerButton.text.toString().trim()

        if (title.isNotEmpty() && date.isNotEmpty() && date != "DD/MM/YY") {
            binding.Done.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        } else {
            binding.Done.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
    }


    private fun setClickedChiped(clickedChip: Chip) {
        val chipList = listOf(
            binding.chipProductivity to "Productivity and Focus",
            binding.chipPersonalGrowth to "Personal Growth",
            binding.chipDailyRoutine to "Daily Routine",
            binding.chipWorkProjects to "Work and Project",
            binding.chipHealthWellness to "Health and Wellness",
        )

        for ((chip, text) in chipList) {
            if (chip == clickedChip) {
                chip.setChipIconResource(R.drawable.categorycheck)
                category=text

            } else {
                chip.setChipIconResource(R.drawable.circle)
            }
        }
    }


    private fun checkVerifyingNullInput(): TaskEntity? {
        val title = binding.editText.text.toString().trim()
        val description = binding.editText2.text.toString().trim()
        val date = binding.datePickerButton.text.toString().trim()
        val startTime = binding.startTimeButton.text.toString().trim()
        val endTime = binding.endTimeButton.text.toString().trim()

        when {
            title.isBlank() -> {
                Toast.makeText(requireContext(), "Please enter a Task Title", Toast.LENGTH_SHORT).show()

            }
            date.isBlank() || date == "DD/MM/YY" -> {
                Toast.makeText(requireContext(), "Please select a Date", Toast.LENGTH_SHORT).show()
            }
            date.isNotBlank() && title.isNotBlank()->{
                binding.Done.setTextColor(requireContext().getColor(R.color.green))
                taskEntity= TaskEntity(0, userId ,title,description,startTime,endTime, date,category)
                showSuccessDialog()

            }
        }
        return taskEntity
    }

    private fun showSuccessDialog() {
        val title = SpannableString("Success!")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val message = SpannableString("Task has been added successfully 🎉")
        message.setSpan(StyleSpan(Typeface.BOLD), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val dialog = android.app.AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialogInterface, _ ->
                    findNavController().navigate(R.id.action_addtask_to_home2)
                    dialogInterface.dismiss()
                }
                .create()
            dialog.show()
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_button)


    }

    fun setTime(textView:TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour >= 12) "PM" else "AM"

                val hourIn12Format = if (selectedHour > 12) selectedHour - 12
                else if (selectedHour == 0) 12
                else selectedHour

                val formattedTime =
                    String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                textView.setText(formattedTime)
            },
            hour, minute, false
        )
        timePicker.show()
    }
}

