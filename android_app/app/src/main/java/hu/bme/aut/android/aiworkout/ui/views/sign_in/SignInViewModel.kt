package hu.bme.aut.android.aiworkout.ui.views.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.aiworkout.domain.AuthRepository
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val _signInState  = Channel<SignInState>()
    val signInState  = _signInState.receiveAsFlow()

    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password).collect {
            when (it) {
                is Resource.Success -> {
                    _signInState.send(SignInState(isLoading = false, isSuccess = it.data?.email))
                }
                is Resource.Error -> {
                    _signInState.send(SignInState(isLoading = false, isError = it.message))
                }
                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }
            }
        }
    }

}