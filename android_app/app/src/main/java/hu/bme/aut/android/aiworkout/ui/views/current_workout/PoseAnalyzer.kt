package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.graphics.*
import android.media.Image
import android.media.Image.Plane
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.PoseDetector
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit
import kotlin.experimental.and


class PoseAnalyzer(private var poseDetector: PoseDetector, private var poseClassifier: PoseClassifier
) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private var lastPose: Int = 0

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        if (image.image == null) {
            return
        }
//        Log.d("PoseAnalyzer", image.planes[0].buffer[0].toString())
//        Log.d("PoseAnalyzer", "Image format: " + image.format.toString())
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >=
            TimeUnit.SECONDS.toMillis(5)
        ) {
            val bitmap = image.convertImageProxyToBitmap()
            for (i in 0..9) {
                for (j in 0..9) {
                    if (bitmap != null) {
                        Log.d("PoseAnalyzer", "x: " + i + " y: " + j + " " + bitmap.getPixel(i, j))
                    }
                }
            }
            // print the content of the bitmap
            Log.d("PoseAnalyzer", "bitmap: $bitmap")

            val person = bitmap?.let { poseDetector.estimatePoses(it) }

            Log.i("PoseAnalyzer", "Person: $person")
            val pose = person.let {
                if (person != null) {
                    poseClassifier.classify(person)
                }
            }
            Log.i("PoseAnalyzer", "Pose: $pose")
            lastAnalyzedTimestamp = currentTimestamp
        }
        image.close()
    }

    interface PoseAnalyzerListener {
        fun onPoseDetected(detections: List<Pair<String, Float>>)
    }
    fun setPoseAnalyzerListener(listener: PoseAnalyzerListener) {
//        this.poseListener = listener::onPoseDetected
    }

    fun ImageProxy.convertImageProxyToBitmap(): Bitmap {
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
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun getBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer[bytes]
        val clonedBytes = bytes.clone()
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.size)
    }

}

//fun Image.toBitmap(): Bitmap {
//    val yBuffer = planes[0].buffer // Y
//    val vuBuffer = planes[2].buffer // VU
//
//    val ySize = yBuffer.remaining()
//    val vuSize = vuBuffer.remaining()
//
//    val nv21 = ByteArray(ySize + vuSize)
//
//    yBuffer.get(nv21, 0, ySize)
//    vuBuffer.get(nv21, ySize, vuSize)
//
//    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
//    val out = ByteArrayOutputStream()
//    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
//    val imageBytes = out.toByteArray()
//    // close the output stream
//    out.close()
//    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//}