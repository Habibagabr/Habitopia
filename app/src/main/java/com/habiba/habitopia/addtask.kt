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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.habiba.habitopia.databinding.FragmentAddtaskBinding
import java.util.Calendar

class addtask : Fragment() {
    private var _binding:FragmentAddtaskBinding?=null
    private val binding  get() = _binding!!
    private lateinit var viewModel:TaskViewModel
    private lateinit var repo: TaskRepo
    private lateinit var database: TaskDatabase
    private lateinit var dao: TaskDAO
    private lateinit var taskEntity: TaskEntity
    private lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        database= TaskDatabase.getDatabase(requireContext())
        dao=database.taskDao()
        repo = TaskRepo(dao)
        viewModel = ViewModelProvider(this, TaskViewModelFactory(repo)).get(TaskViewModel::class.java)
        val sharedPref = context?.getSharedPreferences("MasterPreference", Context.MODE_PRIVATE)
        userId = sharedPref?.getString("userId", null).toString()


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
                    val formattedDate =
                        String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.datePickerButton.setText(formattedDate)
                },
                year, month, day
            )
            //no before date just forward income date
            datePicker.datePicker.minDate = calendar.timeInMillis

            datePicker.show()
        }

        binding.Done.setOnClickListener {

            viewModel.insertTask( checkVerifyingNullInput())

        }
    }

    private fun checkVerifyingNullInput():TaskEntity {
        val title = binding.editText.text.toString().trim()
        val description = binding.editText2.text.toString().trim()
        val date = binding.datePickerButton.text.toString().trim()
        val startTime = binding.startTimeButton.text.toString().trim()
        val endTime = binding.endTimeButton.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter a Task Title", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter a Task Description", Toast.LENGTH_SHORT).show()
            }
            date.isEmpty() || date == "DD/MM/YY" -> {
                Toast.makeText(requireContext(), "Please select a Date", Toast.LENGTH_SHORT).show()
            }
            startTime.isEmpty() || startTime == "00:00" -> {
                Toast.makeText(requireContext(), "Please select a Start Time", Toast.LENGTH_SHORT).show()
            }
            endTime.isEmpty() || endTime == "00:00" -> {
                Toast.makeText(requireContext(), "Please select an End Time", Toast.LENGTH_SHORT).show()
            }
            else -> {
                taskEntity= TaskEntity(0, userId ,title,description,startTime,endTime, date)

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

