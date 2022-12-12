package hu.bme.aut.android.aiworkout.ui.views.current_workout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.pose.Pose

import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.aiworkout.data.KeyPoint
import hu.bme.aut.android.aiworkout.data.Person
import hu.bme.aut.android.aiworkout.data.WorkoutInfo
import hu.bme.aut.android.aiworkout.domain.MoveNet
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.ui.views.sign_in.SignInState
import hu.bme.aut.android.aiworkout.util.YuvToRgbConverter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWorkoutViewModel @Inject constructor(
//    private val repository: UserWorkoutsRepository,
    val moveNet: MoveNet,
    val poseClassifier: PoseClassifier,
    val yuvToRgbConverter: YuvToRgbConverter
): ViewModel() {
    val lastDetectedPose: MutableState<List<Pair<String, Float>>> = mutableStateOf(listOf())
    val lastDetectedKeypoints: MutableState<List<KeyPoint>> = mutableStateOf(listOf())
    fun updateLastDetectedPose(pose: List<Pair<String, Float>>) {
        lastDetectedPose.value = pose
    }
    fun updateLastDetectedKeypoints(keypoints: List<KeyPoint>) {
        lastDetectedKeypoints.value = keypoints
    }
}