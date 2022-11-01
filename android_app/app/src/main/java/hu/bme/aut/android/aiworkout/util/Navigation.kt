package hu.bme.aut.android.aiworkout.util

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.aiworkout.presentation.calendar.CalendarScreen
import hu.bme.aut.android.aiworkout.presentation.current_workout.CurrentWorkoutScreen
import hu.bme.aut.android.aiworkout.presentation.home.HomeScreen
import hu.bme.aut.android.aiworkout.presentation.login.LoginScreen
import hu.bme.aut.android.aiworkout.presentation.splash.SplashScreen
import hu.bme.aut.android.aiworkout.presentation.register.RegisterScreen
import hu.bme.aut.android.aiworkout.presentation.settings.SettingsScreen
import hu.bme.aut.android.aiworkout.presentation.workout_details.WorkoutDetailsScreen
import hu.bme.aut.android.aiworkout.presentation.workouts.WorkoutsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("workouts") {
            WorkoutsScreen(navController)
        }
        composable("calendar") {
            CalendarScreen(navController)
        }
        composable("workout_details") {
            WorkoutDetailsScreen(navController)
        }
        composable("current_workout") {
            CurrentWorkoutScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
    }
}