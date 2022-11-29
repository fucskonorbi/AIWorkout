package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import java.util.concurrent.Executor
import java.util.concurrent.Executors

//class PoseDetectorProcessor(
//    private val context: Context,
//    options: PoseDetectorOptionsBase,
//){
//    private val detector: PoseDetector
//    private val classificationExecutor: Executor
//
//    private var poseClassifierProcessor: PoseClassifierProcessor? = null
//
//    init {
//        detector = PoseDetection.getClient(options)
//        classificationExecutor = Executors.newSingleThreadExecutor()
//    }
//
//    fun stop() {
//        detector.close()
//    }
//
//    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
//        val planeProxy = image.planes[0]
//        val buffer = planeProxy.buffer
//        val bytes = ByteArray(buffer.remaining())
//        buffer[bytes]
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }
//
//    fun processImageProxy(image: ImageProxy, callback: (List<Pair<String, Float>>) -> Unit) {
//        val mlImage = image.image ?: return
//        val bitmap = imageProxyToBitmap(image)
//        val person = bitmap?.let { detector.process(it) }
//        val pose = person?.let { poseClassifierProcessor?.classify(it) }
//        pose?.let { callback(it) }
//    }
//}