package hu.bme.aut.android.aiworkout.data

data class WorkoutInfo(
    val id: Int,
    val duration: Int,
    val number_of_exercises: Int,
    val number_of_reps: Int,
    val date: String,
)
