package hu.bme.aut.android.aiworkout.presentation.workouts

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
        LazyColumn(content = {
            items(state.workouts.size) { index ->
                Log.d("WorkoutsListScreen", "Workout: ${state.workouts[index]}")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, MaterialTheme.colors.onBackground)
                ) {
                    Text(
                        text = "Workout: ${state.workouts[index].datetime}",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.Bold,
                    )
                    for (exercise in state.workouts[index].exercises!!) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Type: ${exercise.type}",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(10.dp),
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "Reps: ${exercise.count}",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(10.dp),
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "Duration: ${exercise.duration}",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                    }
                }
            }
        })
    }
}