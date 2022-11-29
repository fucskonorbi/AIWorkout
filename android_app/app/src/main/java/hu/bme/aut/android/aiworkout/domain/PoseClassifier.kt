package hu.bme.aut.android.aiworkout.domain

import android.content.Context
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import hu.bme.aut.android.aiworkout.data.Device
import hu.bme.aut.android.aiworkout.data.Person
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil


class PoseClassifier(
    private val interpreter: Interpreter,
    private val labels: List<String>
) {
    private val input = interpreter.getInputTensor(0).shape()
    private val output = interpreter.getOutputTensor(0).shape()

    companion object {
        private const val MODEL_FILENAME = "pose_classifier.tflite"
        private const val LABELS_FILENAME = "labels.txt"
        private const val CPU_NUM_THREADS = 4

        fun create(context: Context): PoseClassifier {
            val options = Interpreter.Options().apply {
                setNumThreads(CPU_NUM_THREADS)
            }
            val labels = FileUtil.loadLabels(context, LABELS_FILENAME)
            var interpreter: Interpreter?
            interpreter = createPoseClassifierInterpreter()
            return if (interpreter == null) {
                interpreter = Interpreter(
                    FileUtil.loadMappedFile(context, MODEL_FILENAME),
                    options
                )
                PoseClassifier(interpreter, labels)
            } else {
                PoseClassifier(interpreter, labels)
            }
        }
        private fun createPoseClassifierInterpreter(): Interpreter? {
            var interpreter: Interpreter? = null
            Log.i("PoseAnalyzer", "ADIOS! PoseClassifier Creating interpreter")
            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
                .build()
            FirebaseModelDownloader.getInstance()
                .getModel("pose_classifier", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                    conditions)
                .addOnSuccessListener { model: CustomModel? ->
                    // Download complete. Depending on your app, you could enable the ML
                    // feature, or switch from the local model to the remote model, etc.

                    // The CustomModel object contains the local path of the model file,
                    // which you can use to instantiate a TensorFlow Lite interpreter.
                    val modelFile = model?.file
                    Log.i("PoseAnalyzer", "PoseClassifier Model downloaded")
                    Log.i("PoseAnalyzer", "PoseClassifier Model path: ${modelFile?.absolutePath}")
                    if (modelFile != null) {
                        interpreter = Interpreter(modelFile)
                    }

                }
                .addOnFailureListener { exception: Exception? ->
                    exception?.printStackTrace()
                    Log.i("PoseAnalyzer", "ADIOS! PoseClassifier Failed to download model")
                }
            return interpreter
        }
    }

    fun classify(person: Person?): List<Pair<String, Float>> {
        // Preprocess the pose estimation result to a flat array
        val inputVector = FloatArray(input[1])
        person?.keyPoints?.forEachIndexed { index, keyPoint ->
            inputVector[index * 3] = keyPoint.coordinate.y
            inputVector[index * 3 + 1] = keyPoint.coordinate.x
            inputVector[index * 3 + 2] = keyPoint.score
        }

        // Postprocess the model output to human readable class names
        val outputTensor = FloatArray(output[1])
        interpreter.run(arrayOf(inputVector), arrayOf(outputTensor))
        val output = mutableListOf<Pair<String, Float>>()
        outputTensor.forEachIndexed { index, score ->
            output.add(Pair(labels[index], score))
        }
        return output
    }

    fun close() {
        interpreter.close()
    }


}