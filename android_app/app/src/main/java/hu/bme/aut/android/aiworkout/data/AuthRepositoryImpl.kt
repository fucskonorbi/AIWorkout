package hu.bme.aut.android.aiworkout.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.aiworkout.domain.AuthRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(authResult.user!!)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun register(email: String, password: String): Resource<FirebaseUser> {
        return try{
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(email).build())?.await()
            Resource.Success(authResult.user!!)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "Error")
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

}