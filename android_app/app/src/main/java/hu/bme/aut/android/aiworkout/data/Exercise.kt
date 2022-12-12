package hu.bme.aut.android.aiworkout.data

data class Exercise(
    val type: WorkoutTypes,
    val duration: Int, // in seconds
    val count: Int,
)
