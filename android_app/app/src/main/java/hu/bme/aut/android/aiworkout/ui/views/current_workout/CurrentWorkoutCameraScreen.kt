package hu.bme.aut.android.aiworkout.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.ImageFormat
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import hu.bme.aut.android.aiworkout.data.BodyPart
import hu.bme.aut.android.aiworkout.data.KeyPoint
import hu.bme.aut.android.aiworkout.data.UserWorkoutsRepositoryImpl
import hu.bme.aut.android.aiworkout.domain.MoveNet
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.UserWorkoutsRepository
import hu.bme.aut.android.aiworkout.ui.views.current_workout.CurrentWorkoutViewModel
import hu.bme.aut.android.aiworkout.ui.views.current_workout.PoseAnalyzer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.util.concurrent.Executor
import java.util.concurrent.Executors
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
                            .build()
                            .also {
                                it.setAnalyzer(executor, PoseAnalyzer(CurrentWorkoutViewModel))
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
        val detections = CurrentWorkoutViewModel.lastDetectedPose.value
        LazyColumn(content = {
            items(detections.size) { index ->
                Text(detections[index].toString(),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    color = White
                )
            }
        })
        val poseWithLargeConfidence = detections.maxByOrNull { it.second }
        if (poseWithLargeConfidence != null) {
            Text(poseWithLargeConfidence.first,
                modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
        val bodyJoints = listOf(
            Pair(BodyPart.NOSE, BodyPart.LEFT_EYE),
            Pair(BodyPart.NOSE, BodyPart.RIGHT_EYE),
            Pair(BodyPart.LEFT_EYE, BodyPart.LEFT_EAR),
            Pair(BodyPart.RIGHT_EYE, BodyPart.RIGHT_EAR),
            Pair(BodyPart.NOSE, BodyPart.LEFT_SHOULDER),
            Pair(BodyPart.NOSE, BodyPart.RIGHT_SHOULDER),
            Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_ELBOW),
            Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_WRIST),
            Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
            Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
            Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
            Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
            Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_HIP),
            Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
            Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
            Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
            Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
            Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
        )
        val keypoints = CurrentWorkoutViewModel.lastDetectedKeypoints.value
        if (keypoints.isNotEmpty()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                Log.d("Canvas", "Canvas size: $canvasWidth x $canvasHeight")
                Log.d("Canvas", "Canvas")
                bodyJoints.forEach { joint ->
                    val firstKeypoint: KeyPoint = keypoints[joint.first.position]
                    val secondKeypoint: KeyPoint = keypoints[joint.second.position]
                    drawLine(
                        color = Black,
                        start = Offset(
                            firstKeypoint.coordinate.x * 5.625F,
                            firstKeypoint.coordinate.y * 10.7916F
                        ),
                        end = Offset(
                            secondKeypoint.coordinate.x * 5.625F,
                            secondKeypoint.coordinate.y * 10.7916F
                        ),
                        strokeWidth = 8f
                    )
                }
                drawLine(
                    start = Offset(x = canvasWidth, y = 0f),
                    end = Offset(x = 0f, y = canvasHeight),
                    color = Red,
                    strokeWidth = 8f
                )
            }
        }
    }
}
