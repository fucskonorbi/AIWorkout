package hu.bme.aut.android.aiworkout.data

import com.google.firebase.firestore.CollectionReference
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserWorkoutsRepositoryImpl @Inject constructor(
    private val workoutsReference: CollectionReference
): UserWorkoutsRepository{
    override suspend fun getWorkouts(): Flow<Resource<List<Workout>>> {
        return flow{
            emit(Resource.Loading())
            try {
                val workouts = workoutsReference.get().await().toObjects(Workout::class.java)
                emit(Resource.Success(workouts))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

    override suspend fun deleteWorkout(dateTime: String): Flow<Resource<Workout>> {
        return flow{
            emit(Resource.Loading())
            try {
                val workout = workoutsReference.document(dateTime).get().await().toObject(Workout::class.java)
                workoutsReference.document(dateTime).delete().await()
                emit(Resource.Success(workout!!))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

    override suspend fun addWorkout(workout: Workout): Flow<Resource<Workout>> {
        return flow{
            emit(Resource.Loading())
            try {
                workoutsReference.add(workout.datetime).await()
                emit(Resource.Success(workout))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

    override suspend fun saveWorkout(workout: Workout): Flow<Resource<Workout>> {
        return flow{
            emit(Resource.Loading())
            try {
                workoutsReference.document(workout.datetime).set(workout).await()
                emit(Resource.Success(workout))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

    override suspend fun addDummyWorkout(): Flow<Resource<Workout>>{
        return flow{
            emit(Resource.Loading())
            try {
                val workout = Workout(
                    datetime = Date().toString(),
                    exercises = listOf(
                        Exercise(
                            type = WorkoutTypes.SQUAT,
                            duration = 10,
                            count = 10
                        ),
                        Exercise(
                            type = WorkoutTypes.PUSHUP,
                            duration = 10,
                            count = 10
                        ),
                        Exercise(
                            type = WorkoutTypes.SITUP,
                            duration = 10,
                            count = 10
                        ),
                        Exercise(
                            type = WorkoutTypes.PUSHUP,
                            duration = 10,
                            count = 5
                        ),
                    )
                )
                val uuid: UUID = UUID.randomUUID()
                val str: String = uuid.toString()
                workoutsReference.document("dummy$str").set(workout).await()
                emit(Resource.Success(workout))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
        }
    }

}