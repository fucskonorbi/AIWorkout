package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.content.Context
import android.graphics.*
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import hu.bme.aut.android.aiworkout.domain.MoveNet
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.PoseDetector
import java.util.concurrent.TimeUnit
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors


class PoseAnalyzer(private var poseDetector: PoseDetector, private var poseClassifier: PoseClassifier
) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private var lastPose: Int = 0

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        Log.i("PoseAnalyzer", "Entered analyze")
        if (image.image == null) {
            Log.d("PoseAnalyzer", "Image is null")
            return
        }
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >=
            TimeUnit.SECONDS.toMillis(5)
        ) {
            Log.i("PoseAnalyzer", "Entered if statement")
            val bitmap = image.image!!.toBitmap()
            Log.i("PoseAnalyzer", "Bitmap created")
            val person = bitmap.let { poseDetector.estimatePoses(bitmap) }
            Log.i("PoseAnalyzer", "Person: $person")
            val pose = person.let { poseClassifier.classify(person) }
            Log.i("PoseAnalyzer", "Pose: $pose")
//
//            this.poseListener(pose!!)
//            Log.d("PoseAnalyzer", pose.toString())
            lastAnalyzedTimestamp = currentTimestamp
        }
        Log.i("PoseAnalyzer", "Closing image")
        image.close()
    }

//    private fun createPoseClassifierInterpreter(): Interpreter? {
//        var interpreter: Interpreter? = null
//        Log.i("PoseAnalyzer", "ADIOS! PoseClassifier Creating interpreter")
//        val conditions = CustomModelDownloadConditions.Builder()
//            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
//            .build()
//        FirebaseModelDownloader.getInstance()
//            .getModel("pose_classifier_model.tflite", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
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

    interface PoseAnalyzerListener {
        fun onPoseDetected(detections: List<Pair<String, Float>>)
    }
    fun setPoseAnalyzerListener(listener: PoseAnalyzerListener) {
//        this.poseListener = listener::onPoseDetected
    }

}

fun Image.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer // Y
    val vuBuffer = planes[2].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    // close the output stream
    out.close()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}