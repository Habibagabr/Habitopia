package com.habiba.habitopia.helperFunction

import android.content.Context
import com.habiba.habitopia.DataBase.HabitsEntity

fun insertHabitsFromPreferences(context: Context, userId: String, insertHabit: (HabitsEntity) -> Unit) {
    val prefsNames = listOf("reading_prefs", "sleeping_prefs", "drinking_prefs", "fitness_prefs")

    for (prefName in prefsNames) {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

        val habitName = prefs.getString("habitName", null)
        val answer = prefs.getString("answer", null)
        val count = prefs.getInt("habitCount", -1)
        val emoji = prefs.getString("emoji", "💡") // default emoji if not found

        if (habitName != null) {
            // بناء النص الكامل للوقت (مثلاً: "8 hours", "5 pages", إلخ)
            val time = when {
                count != -1 && answer != null -> "$count - $answer"
                count != -1 -> "$count"
                answer != null -> answer
                else -> "unspecified"
            }

            val habit = HabitsEntity(
                userId = userId,
                habitsTitle = habitName,
                habitsEmoji = emoji ?: "💡",
                habitsTime = time,
                habitDone = 0
            )

            insertHabit(habit) // استخدام دالة إدخال قاعدة البيانات (من ViewModel مثلاً)
            prefs.edit().clear().apply()

        }
    }
}
