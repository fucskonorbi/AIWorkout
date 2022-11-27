package hu.bme.aut.android.aiworkout.domain

import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository{
    val user: FirebaseUser?
    suspend fun login(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun register(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun logout()
}