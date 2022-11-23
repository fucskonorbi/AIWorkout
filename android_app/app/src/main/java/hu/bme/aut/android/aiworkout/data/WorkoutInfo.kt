package hu.bme.aut.android.aiworkout.data

import com.google.type.DateTime
import hu.bme.aut.android.aiworkout.data.WorkoutTypes

data class WorkoutInfo(
    val id: Int,
    val duration: Int, // in seconds
    val pushupCount: Int,
    val squatCount: Int,
    val situpCount: Int,
    val date: DateTime
)
