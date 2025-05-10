package com.habiba.habitopia.ViewModel

import HabitViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.habiba.habitopia.Repository.HabitsRepo

class HabitViewModelFactory(private val repo: HabitsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            return HabitViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
