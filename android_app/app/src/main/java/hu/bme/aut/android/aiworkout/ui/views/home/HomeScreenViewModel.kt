package hu.bme.aut.android.aiworkout.ui.views.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.util.RepositoryCallState
import hu.bme.aut.android.aiworkout.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val repository: UserWorkoutsRepository): ViewModel() {

    val _addDummyWorkoutState = Channel<RepositoryCallState>()
    val addDummyWorkoutState = _addDummyWorkoutState.receiveAsFlow()

    fun addDummyWorkout(){
        Log.d("HomeScreenViewModel", "addDummyWorkout")
        viewModelScope.launch {
            repository.addDummyWorkout()
                .collect{
                    when(it){
                        is Resource.Success ->{
                            _addDummyWorkoutState.send(RepositoryCallState(isSuccess = it.data?.datetime))
                        }
                        is Resource.Loading ->{
                            _addDummyWorkoutState.send(RepositoryCallState(isLoading = true))
                        }
                        is Resource.Error ->{
                            _addDummyWorkoutState.send(RepositoryCallState(isError = it.message))
                        }
                    }
                }
        }
    }
}
