import androidx.lifecycle.*
import com.habiba.habitopia.DataBase.HabitsEntity
import com.habiba.habitopia.Repository.HabitsRepo
import kotlinx.coroutines.launch

class HabitViewModel(private val repo: HabitsRepo) : ViewModel() {

    private val _habitsLiveData = MutableLiveData<List<HabitsEntity>>()
    val habitsLiveData: LiveData<List<HabitsEntity>> = _habitsLiveData

    fun getHabitsForUser(userId: String) {
        viewModelScope.launch {
            val habits = repo.getHabitsForUser(userId)
            _habitsLiveData.postValue(habits)
        }
    }

    fun insertHabit(habit: HabitsEntity) {
        viewModelScope.launch {
            repo.insertHabit(habit)
            getHabitsForUser(habit.userId)
        }
    }

    fun deleteHabit(habit: HabitsEntity) {
        viewModelScope.launch {
            repo.deleteHabit(habit)
            getHabitsForUser(habit.userId)
        }
    }

    fun setHabitDone(habitId: Int, done: Int, userId: String) {
        viewModelScope.launch {
            repo.setHabitDone(habitId, done)
            getHabitsForUser(userId)
        }
    }
}
