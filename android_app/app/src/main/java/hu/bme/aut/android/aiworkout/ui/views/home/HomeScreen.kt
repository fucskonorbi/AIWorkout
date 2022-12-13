package hu.bme.aut.android.aiworkout.presentation.home

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddHomeWork
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.destinations.CurrentWorkoutInitialScreenDestination
import hu.bme.aut.android.aiworkout.destinations.SettingsScreenDestination
import hu.bme.aut.android.aiworkout.destinations.WorkoutsListScreenDestination
import hu.bme.aut.android.aiworkout.presentation.workouts.WorkoutsListScreen
import hu.bme.aut.android.aiworkout.ui.views.home.HomeScreenViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val state = viewModel.addDummyWorkoutState.collectAsState(initial = null)
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .weight(0.5f)
        ) {
            Text(
                text = "Welcome to AIWorkout!",
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = { navigator.navigate(CurrentWorkoutInitialScreenDestination) },
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevation(3.dp, 5.dp, 1.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Start workout",
                        tint = Color.Black,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "Start workout",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
            Button(
                onClick = { navigator.navigate(SettingsScreenDestination)},
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevation(3.dp, 5.dp, 1.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Settings",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = { navigator.navigate(WorkoutsListScreenDestination)},
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevation(3.dp, 5.dp, 1.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = "See history",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "See history",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
            Button(
                onClick = { scope.launch { viewModel.addDummyWorkout() } },
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevation(3.dp, 5.dp, 1.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddHomeWork,
                        contentDescription = "Create dummy data",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Create dummy data",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    Toast.makeText(context, "Dummy data created", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}