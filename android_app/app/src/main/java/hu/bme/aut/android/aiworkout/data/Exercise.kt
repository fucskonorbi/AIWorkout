package hu.bme.aut.android.aiworkout.data

data class Exercise(
    val type: WorkoutTypes? = null,
    val duration: Int? = null, // in seconds
    val count: Int? = null,
)
