package hu.bme.aut.android.aiworkout.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector,
    val title: String){
    object Home : BottomBarScreen("home", Icons.Rounded.Home, "Home")
    object Workouts : BottomBarScreen("workouts", Icons.Rounded.List, "Workouts")
    object Calendar : BottomBarScreen("calendar", Icons.Rounded.DateRange, "Calendar")
}
