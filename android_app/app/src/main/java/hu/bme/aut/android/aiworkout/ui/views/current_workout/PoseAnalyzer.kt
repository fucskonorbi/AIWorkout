package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.graphics.*
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import hu.bme.aut.android.aiworkout.domain.PoseClassifier
import hu.bme.aut.android.aiworkout.domain.PoseDetector
import hu.bme.aut.android.aiworkout.util.YuvToRgbConverter
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import kotlin.experimental.and


class PoseAnalyzer(private var poseDetector: PoseDetector, private var poseClassifier: PoseClassifier,
                   private var yuvToRgbConverter: YuvToRgbConverter
) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private var lastPose: Int = 0
    private lateinit var imageBitmap: Bitmap

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        if (image.image == null) {
            return
        }
//        val inputImage = InputImage.fromMediaImage(image.image, image.imageInfo.rotationDegrees)
//        Log.d("PoseAnalyzer", image.planes[0].buffer[0].toString())
        Log.d("PoseAnalyzer", "Image format: " + image.format.toString())
        val currentTimestamp = System.currentTimeMillis()
//        if (currentTimestamp - lastAnalyzedTimestamp >=
//            TimeUnit.SECONDS.toMillis(5)
//        ) {
//            val bitmap = toBitmapRGBA(image.image!!)
        if (!::imageBitmap.isInitialized) {
            imageBitmap =
                Bitmap.createBitmap(
                    image.width,
                    image.height,
                    Bitmap.Config.ARGB_8888
                )
        }
        yuvToRgbConverter.yuvToRgb(image.image!!, imageBitmap)
        // print the content of the bitmap
        val person = imageBitmap.let { poseDetector.estimatePoses(it) }
        Log.i("PoseAnalyzer", "Person: $person")
        val pose = person.let {
            poseClassifier.classify(person)
        }
        Log.i("PoseAnalyzer", "Pose: $pose")
        lastAnalyzedTimestamp = currentTimestamp
//        }
        imageBitmap.eraseColor(Color.TRANSPARENT);
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

    private fun getBitmapAlt(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer[bytes]
        val clonedBytes = bytes.clone()
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.size)
    }
    private fun toBitmapRGBA(image: Image): Bitmap {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width
        val bitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride,
            image.height, Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    fun bitmapFromRgba(width: Int, height: Int, bytes: ByteArray): Bitmap? {
        val pixels = IntArray(bytes.size / 4)
        var j = 0
        for (i in pixels.indices) {
            val R: Int = (bytes[j++] and 0xFF.toByte()).toInt()
            val G: Int = (bytes[j++] and 0xFF.toByte()).toInt()
            val B: Int = (bytes[j++] and 0xFF.toByte()).toInt()
            val A: Int = (bytes[j++] and 0xFF.toByte()).toInt()
            val pixel = A shl 24 or (R shl 16) or (G shl 8) or B
            pixels[i] = pixel
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
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