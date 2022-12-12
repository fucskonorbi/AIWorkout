package hu.bme.aut.android.aiworkout.domain

import com.google.type.DateTime
import hu.bme.aut.android.aiworkout.data.Workout
import hu.bme.aut.android.aiworkout.data.WorkoutInfo
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.Flow


interface UserWorkoutsRepository {
    suspend fun getWorkouts(): Flow<Resource<List<Workout>>>
    suspend fun deleteWorkout(dateTime: String): Flow<Resource<Workout>>
    suspend fun addWorkout(workout: Workout): Flow<Resource<Workout>>
    suspend fun saveWorkout(workout: Workout): Flow<Resource<Workout>>
    suspend fun addDummyWorkout(): Flow<Resource<Workout>>
}