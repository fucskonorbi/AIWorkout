package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.graphics.*
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.PoseDetector
import hu.bme.aut.android.aiworkout.util.YuvToRgbConverter


class PoseAnalyzer(private var poseDetector: PoseDetector, private var poseClassifier: PoseClassifier,
                   private var yuvToRgbConverter: YuvToRgbConverter
) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private lateinit var imageBitmap: Bitmap
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
        yuvToRgbConverter.yuvToRgb(image.image!!, imageBitmap)
        val person = imageBitmap.let { poseDetector.estimatePoses(it) }
        Log.i("PoseAnalyzer", "Person: $person")
        val pose = person.let {
            poseClassifier.classify(person)
        }
        Log.i("PoseAnalyzer", "Pose: $pose")
        lastAnalyzedTimestamp = currentTimestamp
        image.close()
    }
}