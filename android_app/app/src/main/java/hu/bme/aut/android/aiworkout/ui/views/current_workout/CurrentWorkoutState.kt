package hu.bme.aut.android.aiworkout.ui.views.current_workout

import com.google.type.DateTime

data class CurrentWorkoutState(
    val duration: Int, // in seconds
    val pushupCount: Int,
    val squatCount: Int,
    val situpCount: Int,
    val date: DateTime
)