package com.habiba.habitopia

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import java.util.Calendar

class addtask : Fragment() {
    private lateinit var startTimeEditText: TextView
    private lateinit var EndTimeEditText: TextView
    private lateinit var DateEditText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimeEditText = view.findViewById(R.id.startTimeButton)
        EndTimeEditText = view.findViewById(R.id.endTimeButton)
        DateEditText = view.findViewById(R.id.datePickerButton)

        startTimeEditText.setOnClickListener {
            setTime(startTimeEditText)
        }
        EndTimeEditText.setOnClickListener {
            setTime(EndTimeEditText)
        }
        DateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate =
                        String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    DateEditText.setText(formattedDate)
                },
                year, month, day
            )
            datePicker.show()
        }

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
            hour, minute, false // <<< هنا حطينا false علشان يبقى 12 ساعة مش 24 ساعة
        )
        timePicker.show()
    }
}

