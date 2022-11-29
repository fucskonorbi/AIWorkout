package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import hu.bme.aut.android.aiworkout.domain.MoveNet
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import java.util.concurrent.TimeUnit
import org.tensorflow.lite.Interpreter
import java.util.concurrent.Executors


class PoseAnalyzer(ctx: Context) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private var lastPose: Int = 0
//    private var poseClassifierInterpreter: Interpreter? = createPoseClassifierInterpreter()
    private var poseClassifier: PoseClassifier = PoseClassifier.create(context = ctx)
//    private var MoveNetInterpreter: Interpreter? = createMoveNetInterpreter()
    private var poseDetector: MoveNet = MoveNet.create(context = ctx)
//    private lateinit var poseListener: List<Pair<String, Float>>.() -> Unit

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        if (image.image == null) {
            return
        }
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >=
            TimeUnit.SECONDS.toMillis(50)
        ) {
            val bitmap = imageProxyToBitmap(image)
            val person = bitmap?.let { poseDetector.estimatePoses(bitmap) }
            val pose = person?.let { poseClassifier.classify(person) }

//            this.poseListener(pose!!)
            Log.d("PoseAnalyzer", pose.toString())
            lastAnalyzedTimestamp = currentTimestamp
        }
    }

//    private fun createPoseClassifierInterpreter(): Interpreter? {
//        var interpreter: Interpreter? = null
//        Log.i("PoseAnalyzer", "ADIOS! PoseClassifier Creating interpreter")
//        val conditions = CustomModelDownloadConditions.Builder()
//            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
//            .build()
//        FirebaseModelDownloader.getInstance()
//            .getModel("pose_classifier.tflite", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
//                conditions)
//            .addOnSuccessListener { model: CustomModel? ->
//                // Download complete. Depending on your app, you could enable the ML
//                // feature, or switch from the local model to the remote model, etc.
//
//                // The CustomModel object contains the local path of the model file,
//                // which you can use to instantiate a TensorFlow Lite interpreter.
//                val modelFile = model?.file
//                Log.i("PoseAnalyzer", "PoseClassifier Model downloaded")
//                Log.i("PoseAnalyzer", "PoseClassifier Model path: ${modelFile?.absolutePath}")
//                if (modelFile != null) {
//                    interpreter = Interpreter(modelFile)
//                }
//
//            }
//            .addOnFailureListener { exception: Exception? ->
//                exception?.printStackTrace()
//                Log.i("PoseAnalyzer", "ADIOS! PoseClassifier Failed to download model")
//            }
//        return interpreter
//    }

//    private fun createMoveNetInterpreter(): Interpreter? {
//        var interpreter: Interpreter? = null
//        Log.i("PoseAnalyzer", "BONJOUR! Creating interpreter")
//        val conditions = CustomModelDownloadConditions.Builder()
//            .build()
//        FirebaseModelDownloader.getInstance()
//            .getModel("lite-model_movenet_singlepose_lightning_3.tflite", DownloadType.LOCAL_MODEL,
//                conditions)
//            .addOnSuccessListener { model: CustomModel? ->
//                // Download complete. Depending on your app, you could enable the ML
//                // feature, or switch from the local model to the remote model, etc.
//
//                // The CustomModel object contains the local path of the model file,
//                // which you can use to instantiate a TensorFlow Lite interpreter.
//                val modelFile = model?.file
//                Log.i("PoseAnalyzer", "Model downloaded")
//                // log the model file path.
//                Log.i("PoseAnalyzer", modelFile.toString())
//                if (modelFile != null) {
//                    interpreter = Interpreter(modelFile)
//                }
//
//            }
//            .addOnFailureListener { exception: Exception? ->
//                exception?.printStackTrace()
//                Log.i("PoseAnalyzer", "Failed to download model")
//            }
//        return interpreter
//    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        val planeProxy = image.planes[0]
        val buffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer[bytes]
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    interface PoseAnalyzerListener {
        fun onPoseDetected(detections: List<Pair<String, Float>>)
    }
    fun setPoseAnalyzerListener(listener: PoseAnalyzerListener) {
//        this.poseListener = listener::onPoseDetected
    }

    init {
        var context = ctx
    }
}