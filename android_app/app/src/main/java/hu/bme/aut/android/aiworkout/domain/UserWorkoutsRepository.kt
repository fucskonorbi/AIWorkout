package hu.bme.aut.android.aiworkout.domain

import hu.bme.aut.android.aiworkout.data.WorkoutInfo
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.Flow


interface UserWorkoutsRepository {
    suspend fun getWorkouts(): Flow<Resource<List<WorkoutInfo>>>
    suspend fun deleteWorkout(id: Int): Flow<Resource<WorkoutInfo>>
    suspend fun addWorkout(workoutInfo: WorkoutInfo): Flow<Resource<WorkoutInfo>>
    suspend fun saveWorkout(workoutInfo: WorkoutInfo): Flow<Resource<WorkoutInfo>>
}