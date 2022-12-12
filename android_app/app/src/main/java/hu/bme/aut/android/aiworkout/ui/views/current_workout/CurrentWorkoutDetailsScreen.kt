package hu.bme.aut.android.aiworkout.presentation.current_workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.destinations.CurrentWorkoutInitialScreenDestination
//import hu.bme.aut.android.aiworkout.destinations.CurrentWorkoutCameraScreenDestination
import hu.bme.aut.android.aiworkout.ui.views.current_workout.CurrentWorkoutViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun CurrentWorkoutDetailsScreen(
    navigator: DestinationsNavigator,
    currentWorkoutViewModel: CurrentWorkoutViewModel = hiltViewModel(),
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                navigator.navigate(CurrentWorkoutInitialScreenDestination)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
        ) {
            Text(text = "Start workout")
        }
    }
}