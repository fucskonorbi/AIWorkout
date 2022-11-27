package hu.bme.aut.android.aiworkout.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.aiworkout.domain.AuthRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                emit(Resource.Success(result.user!!))
            } catch (e: Exception) {
                emit(Resource.Error(e.message!!))
            }
        }
    }

    override suspend fun register(email: String, password: String): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user!!
                emit(Resource.Success(user))
            } catch (e: Exception) {
                emit(Resource.Error(e.message!!))
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

}