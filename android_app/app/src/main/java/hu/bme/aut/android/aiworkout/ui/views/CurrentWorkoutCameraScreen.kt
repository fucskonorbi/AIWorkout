package hu.bme.aut.android.aiworkout.ui.views

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.concurrent.Executor

@Destination
@Composable
fun CurrentWorkoutCameraScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier,
    executor: Executor,
    context: Context,
    imageCapture: MutableState<ImageCapture?>,
) {
    val previewCameraView = remember { PreviewView(context) }
    val cameraProviderFuture =
        remember(context) { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = remember(cameraProviderFuture) { cameraProviderFuture.get() }
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { context ->
                cameraProviderFuture.addListener(
                    {
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                            .build()
                        imageCapture.value = ImageCapture.Builder().build()
                        cameraProvider.unbindAll()
                        val prev = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewCameraView.surfaceProvider)
                        }

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            prev,
                            imageCapture.value
                        )
                    }, executor
                )
                previewCameraView
            }
        )
    }
}