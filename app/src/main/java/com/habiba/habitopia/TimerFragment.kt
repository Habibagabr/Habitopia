package com.habiba.habitopia

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class TimerFragment : Fragment() {

    private lateinit var tvTimer: TextView
    private lateinit var progressCircle: ProgressBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnReset: ImageButton
    private lateinit var btnSetTime: ImageButton

    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var timeLeftInMillis: Long = 0L
    private var totalTimeInMillis: Long = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_timer, container, false)

        tvTimer = rootView.findViewById(R.id.tvTimer)
        progressCircle = rootView.findViewById(R.id.progressCircle)
        btnPlayPause = rootView.findViewById(R.id.btnPlayPause)
        btnReset = rootView.findViewById(R.id.btnReload)
        btnSetTime = rootView.findViewById(R.id.btnSetTime)

        updateTimerUI()

        btnPlayPause.setOnClickListener { if (isRunning) pauseTimer() else startTimer() }
        btnReset.setOnClickListener { resetTimer() }
        btnSetTime.setOnClickListener { showCustomTimePicker() }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        return rootView
    }

    private fun showCustomTimePicker() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom_timer, null)

        val minutesPicker = dialogView.findViewById<NumberPicker>(R.id.minutesPicker)
        val secondsPicker = dialogView.findViewById<NumberPicker>(R.id.secondsPicker)
        val btnSet = dialogView.findViewById<Button>(R.id.btnSet)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59
        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59

        customizeNumberPicker(minutesPicker, 28f, ContextCompat.getColor(requireContext(), R.color.light_beige))
            customizeNumberPicker(secondsPicker, 28f, ContextCompat.getColor(requireContext(),R.color.light_beige))

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnSet.setOnClickListener {
            val minutes = minutesPicker.value
            val seconds = secondsPicker.value
            totalTimeInMillis = (minutes * 60 + seconds) * 1000L
            timeLeftInMillis = totalTimeInMillis
            updateTimerUI()
            startTimer()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }

    private fun customizeNumberPicker(numberPicker: NumberPicker, textSize: Float, textColor: Int) {
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val inputText = numberPicker.getChildAt(i)
            if (inputText is EditText) {
                inputText.setTextColor(textColor)
                inputText.textSize = textSize
                inputText.setTypeface(Typeface.DEFAULT_BOLD)
                inputText.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
                inputText.isCursorVisible = false
            }
        }

        try {
            val fields = numberPicker.javaClass.declaredFields
            for (field in fields) {
                if (field.name == "mSelectionDivider") {
                    field.isAccessible = true
                    field.set(numberPicker, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startTimer() {
        if (timeLeftInMillis == 0L) {
            Toast.makeText(requireContext(), "Please set a valid time", Toast.LENGTH_SHORT).show()
            return
        }
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                isRunning = false
                timeLeftInMillis = 0L
                updateTimerUI()
                progressCircle.progress = 100
                btnPlayPause.setImageResource(R.drawable.play)
                showMotivationalMessage()
                sendNotification()
            }
        }.start()
        btnPlayPause.setImageResource(R.drawable.pause)
        isRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
        btnPlayPause.setImageResource(R.drawable.play)
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = 0L
        totalTimeInMillis = 0L
        updateTimerUI()
        isRunning = false
        btnPlayPause.setImageResource(R.drawable.play)
    }

    private fun updateTimerUI() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        tvTimer.text = String.format("%02d:%02d", minutes, seconds)
        val progress = if (totalTimeInMillis > 0) {
            (((totalTimeInMillis - timeLeftInMillis).toFloat() / totalTimeInMillis) * 100).toInt()
        } else 0
        progressCircle.progress = progress
    }

    private fun showMotivationalMessage() {
        if (!isAdded || view == null) return
        view?.let { rootView ->
            Snackbar.make(rootView, "Great job! You did it! Keep up!", Snackbar.LENGTH_LONG)
                .setBackgroundTint(resources.getColor(R.color.blue))
                .setTextColor(resources.getColor(R.color.white))
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

    private fun sendNotification() {
        val channelId = "timer_channel"
        val notificationId = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Notifications"
            val descriptionText = "Channel for timer finished notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val intent = requireActivity().intent
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.done)
            .setContentTitle("Timer Finished")
            .setContentText("Well done! Your timer is complete.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(requireContext()).notify(notificationId, builder.build())
        } else {
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
