package hu.bme.aut.android.aiworkout.data

import com.google.firebase.firestore.CollectionReference
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserWorkoutsRepositoryImpl @Inject constructor(
    private val workoutsReference: CollectionReference
): UserWorkoutsRepository{
    override suspend fun getWorkouts(): Flow<Resource<List<WorkoutInfo>>> {
        return flow{
            emit(Resource.Loading())
            try {
                val workouts = workoutsReference.get().await().toObjects(WorkoutInfo::class.java)
                emit(Resource.Success(workouts))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

    override suspend fun deleteWorkout(id: Int): Flow<Resource<WorkoutInfo>> {
        return flow{
            emit(Resource.Loading())
            try {
                val workout = workoutsReference.document(id.toString()).get().await().toObject(WorkoutInfo::class.java)
                workoutsReference.document(id.toString()).delete().await()
                emit(Resource.Success(workout!!))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

    override suspend fun addWorkout(workoutInfo: WorkoutInfo): Flow<Resource<WorkoutInfo>> {
        return flow{
            emit(Resource.Loading())
            try {
                workoutsReference.document(workoutInfo.id.toString()).set(workoutInfo).await()
                emit(Resource.Success(workoutInfo))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

}