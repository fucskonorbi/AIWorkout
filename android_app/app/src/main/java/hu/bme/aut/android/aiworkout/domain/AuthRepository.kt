package hu.bme.aut.android.aiworkout.domain

import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.aiworkout.util.Resource

interface AuthRepository{
    val user: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun register(email: String, password: String): Resource<FirebaseUser>
    suspend fun logout()
}