package hu.bme.aut.android.aiworkout.ui.views.workout_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.aiworkout.data.Workout
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutsListViewModel @Inject constructor(
    val repository: UserWorkoutsRepository
): ViewModel() {

    var state by mutableStateOf(WorkoutListState())

    init {
        getWorkouts()
    }

    private fun getWorkouts() = viewModelScope.launch {
        repository.getWorkouts().collect { result ->
            Log.d("WorkoutsListViewModel", "getWorkouts: $result")
            when (result) {
                is Resource.Success -> {
                    state = state.copy(
                        isLoading = false,
                        workouts = result.data ?: listOf()
                    )
                }
                is Resource.Loading -> {
                    state = state.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    Log.d("WorkoutsListViewModel", "getWorkouts: ${result.message}")
                    state = state.copy(
                        isLoading = false,
                        isError = result.message ?: "Error"
                    )
                }
            }
        }
    }
}