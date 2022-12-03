package hu.bme.aut.android.aiworkout.ui.views.current_workout

import android.content.Context
import android.graphics.*
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import hu.bme.aut.android.aiworkout.ml.LiteModelMovenetSingleposeLightningTfliteInt84
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit


class PoseAnalyzer(ctx: Context) : ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    private val model = LiteModelMovenetSingleposeLightningTfliteInt84.newInstance(ctx)
    val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR))
        .build()
    private val context = ctx
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        if (image.image == null) {
            return
        }
        val bitmapImage = this.toBitmap(image.image!!)
        val tensorImage = TensorImage.fromBitmap(bitmapImage)
        val processedImage = imageProcessor.process(tensorImage)
        val byteBuffer = processedImage.buffer

        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >=
            TimeUnit.SECONDS.toMillis(50)
        ) {
            Log.i("PoseAnalyzer", "image: ${image.image}")

            val person = this.runMoveNetDetection(byteBuffer, context)
            Log.i("PoseAnalyzer", "person: $person")
            lastAnalyzedTimestamp = currentTimestamp
        }

        image.close()
    }

    private fun runMoveNetDetection(byteBuffer: ByteBuffer, context: Context): TensorBuffer {
        val model = LiteModelMovenetSingleposeLightningTfliteInt84.newInstance(context)
        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 192, 192, 3), DataType.UINT8)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        return outputFeature0
    }

    init {
        var context = ctx
    }

//    private var bitmapBuffer: Bitmap? = null
//    private fun toBitmap(image: ImageProxy): Bitmap? {
//        if (bitmapBuffer == null) {
//            bitmapBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
//        }
//        bitmapBuffer?.copyPixelsFromBuffer(image.planes[0].buffer)
//        return bitmapBuffer
//    }

    private fun toBitmap(image: Image): Bitmap? {
        val planes: Array<Image.Plane> = image.getPlanes()
        val yBuffer: ByteBuffer = planes[0].getBuffer()
        val uBuffer: ByteBuffer = planes[1].getBuffer()
        val vBuffer: ByteBuffer = planes[2].getBuffer()
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        val imageBytes: ByteArray = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}
