package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.graphics.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.pose.Pose
import hu.bme.aut.android.aiworkout.data.Person
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.PoseDetector
import hu.bme.aut.android.aiworkout.util.YuvToRgbConverter


class PoseAnalyzer(var viewmodel: CurrentWorkoutViewModel
) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private lateinit var imageBitmap: Bitmap
    @RequiresApi(Build.VERSION_CODES.O)
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        if (image.image == null) {
            return
        }
        Log.d("PoseAnalyzer", "Image format: " + image.format.toString())
        val currentTimestamp = System.currentTimeMillis()
        if (!::imageBitmap.isInitialized) {
            imageBitmap =
                Bitmap.createBitmap(
                    image.width,
                    image.height,
                    Bitmap.Config.ARGB_8888
                )
        }
        viewmodel.yuvToRgbConverter.yuvToRgb(image.image!!, imageBitmap)
        val person: Person = imageBitmap.let { viewmodel.moveNet.estimatePoses(it) }
        viewmodel.updateLastDetectedKeypoints(person.keyPoints)
        Log.i("PoseAnalyzer", "Person: $person")
        val pose = person.let {
            viewmodel.poseClassifier.classify(person)
        }

        Log.i("PoseAnalyzer", "Pose: $pose")
        lastAnalyzedTimestamp = currentTimestamp
        viewmodel.updateLastDetectedPose(pose)
        image.close()
    }
}