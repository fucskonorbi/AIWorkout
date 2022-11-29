package hu.bme.aut.android.aiworkout.ui.views.current_workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.aiworkout.data.WorkoutInfo
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.ui.views.sign_in.SignInState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWorkoutViewModel @Inject constructor(
    private val repository: UserWorkoutsRepository
): ViewModel() {
    private val _currentWorkoutState = Channel<CurrentWorkoutState>()
    val currentWorkoutState  = _currentWorkoutState.receiveAsFlow()

}