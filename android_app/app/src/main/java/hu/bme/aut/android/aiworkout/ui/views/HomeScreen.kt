package hu.bme.aut.android.aiworkout.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController
) {
    Row {
        // 2 boxes for Start Workout and Settings
        Box(contentAlignment = Alignment.Center) {
            TextButton(
                onClick = {
                    navController.navigate("current_workout")
                          },
                content = {
                    Text("Start Workout")
                          },
                modifier = Modifier.padding(16.dp))
        }
    }
}