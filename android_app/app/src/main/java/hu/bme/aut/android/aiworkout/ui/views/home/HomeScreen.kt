package hu.bme.aut.android.aiworkout.presentation.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddHomeWork
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.destinations.WorkoutDetailsScreenDestination


@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = "Welcome to AI Workout",
            fontSize = 30.sp,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navigator.navigate(WorkoutDetailsScreenDestination) },
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
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
                onClick = { navigator.navigate(WorkoutDetailsScreenDestination)},
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navigator.navigate(WorkoutDetailsScreenDestination)},
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
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
                onClick = { navigator.navigate(WorkoutDetailsScreenDestination)},
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.History,
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

    }
}