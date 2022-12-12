package hu.bme.aut.android.aiworkout.presentation.workouts

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.ui.views.home.HomeScreenViewModel
import hu.bme.aut.android.aiworkout.ui.views.workout_list.WorkoutsListViewModel

@Destination
@Composable
fun WorkoutsListScreen(
    navigator: DestinationsNavigator,
    viewModel: WorkoutsListViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Workouts",
            fontSize = 30.sp,
            modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally)
        )
        Log.d("WorkoutsListScreen", "Workouts: ${state.workouts}")
        LazyColumn(content = {
            items(state.workouts.size) { index ->
                Log.d("WorkoutsListScreen", "Workout: ${state.workouts[index]}")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Date: ${state.workouts[index].datetime}",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        Log.d("WorkoutsListScreen", "Workout: ${state.workouts[index]}")
                        for (i in 0 until (state.workouts[index].exercises?.size ?: 0)) {
                            Text(
                                text = "Exercise: ${state.workouts[index].exercises?.get(i)?.type}",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Text(
                                text = "Reps: ${state.workouts[index].exercises?.get(i)?.count}",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Text(
                                text = "Weight: ${state.workouts[index].exercises?.get(i)?.duration}",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }
        })
    }
}