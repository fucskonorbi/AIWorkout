package hu.bme.aut.android.aiworkout.ui.views

import android.content.Context
import android.graphics.ImageFormat
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.data.UserWorkoutsRepositoryImpl
import hu.bme.aut.android.aiworkout.domain.MoveNet
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.ui.views.current_workout.CurrentWorkoutViewModel
import hu.bme.aut.android.aiworkout.ui.views.current_workout.PoseAnalyzer
import kotlinx.coroutines.delay
import java.util.concurrent.Executor
import kotlin.time.Duration.Companion.seconds


@ExperimentalPermissionsApi
@Destination(start=true)
@Composable
fun CurrentWorkoutInitialScreen(
    navigator: DestinationsNavigator,
    CurrentWorkoutViewModel: CurrentWorkoutViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            LaunchedEffect(Unit) {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            Column {
                Text(
                    "Camera permission denied."
                )
            }
        }
    ) {
        CurrentWorkoutCameraScreen(navigator, context, lifecycleOwner, CurrentWorkoutViewModel)
    }
}



@Composable
fun CurrentWorkoutCameraScreen(
    navigator: DestinationsNavigator,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    CurrentWorkoutViewModel: CurrentWorkoutViewModel
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context)}
    val previewCameraView = remember { PreviewView(context) }
    val cameraProvider = remember(cameraProviderFuture) { cameraProviderFuture.get() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val executor = ContextCompat.getMainExecutor(context)
                cameraProviderFuture.addListener(
                    {
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                            .build()
                        cameraProvider.unbindAll()
                        val prev = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewCameraView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(previewView.width/2, previewView.height/2))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .setImageQueueDepth(10)
//                            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                            .build()
                            .also {
                                it.setAnalyzer(executor, PoseAnalyzer(CurrentWorkoutViewModel.moveNet, CurrentWorkoutViewModel.poseClassifier,
                                    CurrentWorkoutViewModel.yuvToRgbConverter))
                            }

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            prev,
                            imageAnalysis
                        )
                    }, executor
                )
                previewCameraView
            }
        )

    }
}
