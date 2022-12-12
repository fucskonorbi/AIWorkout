package hu.bme.aut.android.aiworkout.ui.views.workout_list

import hu.bme.aut.android.aiworkout.data.Workout

data class WorkoutListState(
    val workouts: List<Workout> = emptyList(),
    val isLoading: Boolean = false,
    val isError: String? = null,
)
