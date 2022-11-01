package hu.bme.aut.android.aiworkout.util

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Register : Screen("register")
    object Login : Screen("login")
    object Home : Screen("home")
    object Workouts : Screen("workouts")
    object Calendar : Screen("calendar")
    object WorkoutDetails : Screen("workout_details")
    object CurrentWorkout : Screen("current_workout")
    object Settings : Screen("settings")
}
