package hu.bme.aut.android.aiworkout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.aiworkout.domain.AuthRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val currentUser: FirebaseUser?
        get() = authRepository.user

    private val _registerState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerState: StateFlow<Resource<FirebaseUser>?> = _registerState

    private val _loginState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginState: StateFlow<Resource<FirebaseUser>?> = _loginState

    init {
        if (authRepository.user != null){
            _loginState.value = Resource.Success(authRepository.user!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = Resource.Loading()
        val result = authRepository.login(email, password)
        _loginState.value = result
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _registerState.value = Resource.Loading()
        val result = authRepository.register(email, password)
        _registerState.value = result
    }


}