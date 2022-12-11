package hu.bme.aut.android.aiworkout.domain

import android.R.attr
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import hu.bme.aut.android.aiworkout.data.BodyPart
import hu.bme.aut.android.aiworkout.data.Device
import hu.bme.aut.android.aiworkout.data.KeyPoint
import hu.bme.aut.android.aiworkout.data.Person
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


enum class ModelType {
    Lightning,
    Thunder
}

class MoveNet(
    private val interpreter: Interpreter): PoseDetector {

    private val inputWidth = interpreter.getInputTensor(0).shape()[1]
    private val inputHeight = interpreter.getInputTensor(0).shape()[2]
    private var outputShape: IntArray = interpreter.getOutputTensor(0).shape()
    private var lastInferenceTimeNanos: Long = -1

    companion object {
        private const val CPU_NUM_THREADS = 4

        // TFLite file names.
        private const val LIGHTNING_FILENAME = "movenet_lightning.tflite"
        private const val THUNDER_FILENAME = "movenet_thunder.tflite"

        fun create(context: Context, device: Device, modelType: ModelType): MoveNet {
            val options = Interpreter.Options()
            options.setNumThreads(CPU_NUM_THREADS)
            var gpuDelegate: GpuDelegate? = null

            when (device) {
                Device.CPU -> {
                }
                Device.GPU -> {
                    gpuDelegate = GpuDelegate()
                    options.addDelegate(gpuDelegate)
                }
            }
            return MoveNet(
                Interpreter(
                    FileUtil.loadMappedFile(
                        context,
                        when (modelType) {
                            ModelType.Lightning -> LIGHTNING_FILENAME
                            ModelType.Thunder -> THUNDER_FILENAME
                        }
                    ),
                    options
                )
            )
        }

        fun create(context: Context, device: Device): MoveNet =
            create(context, device, ModelType.Lightning)

        fun create(context: Context): MoveNet =
            create(context, Device.CPU, ModelType.Lightning)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun estimatePoses(bitmap: Bitmap): Person {
        val inferenceStartTimeNanos = SystemClock.elapsedRealtimeNanos()
        var totalScore = 0f

//        val width: Int = bitmap.width
//        val height: Int = bitmap.height
//        val pixels = IntArray(width * height)
//        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
//        var sumRed: Long = 0
//        var sumGreen: Long = 0
//        var sumBlue: Long = 0
//        for (pixel in pixels) {
//            // Get the red, green, and blue values of the pixel
//            val red: Int = Color.red(pixel)
//            val green: Int = Color.green(pixel)
//            val blue: Int = Color.blue(pixel)
//
//            // Add the red, green, and blue values to the sum
//            sumRed += red.toLong()
//            sumGreen += green.toLong()
//            sumBlue += blue.toLong()
//        }
//        val meanRed = (sumRed / pixels.size).toDouble()
//        val meanGreen = (sumGreen / pixels.size).toDouble()
//        val meanBlue = (sumBlue / pixels.size).toDouble()
//        Log.d("mean", "meanRed: $meanRed, meanGreen: $meanGreen, meanBlue: $meanBlue")

        val numKeyPoints = outputShape[2]
        val keyPoints = mutableListOf<KeyPoint>()

        val detectBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        val width: Int = detectBitmap.width
        val height: Int = detectBitmap.height
        val pixels = IntArray(width * height)
        detectBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var sumRed: Long = 0
        var sumGreen: Long = 0
        var sumBlue: Long = 0
        for (pixel in pixels) {
            // Get the red, green, and blue values of the pixel
            val red: Int = Color.red(pixel)
            val green: Int = Color.green(pixel)
            val blue: Int = Color.blue(pixel)

            // Add the red, green, and blue values to the sum
            sumRed += red.toLong()
            sumGreen += green.toLong()
            sumBlue += blue.toLong()
        }
        val meanRed = (sumRed / pixels.size).toDouble()
        val meanGreen = (sumGreen / pixels.size).toDouble()
        val meanBlue = (sumBlue / pixels.size).toDouble()
        Log.d("meandetectbitmap", "meanRed: $meanRed, meanGreen: $meanGreen, meanBlue: $meanBlue")
        Log.d("bitmap", "format: " + detectBitmap.config + detectBitmap.colorSpace)
        val inputTensor = processInputImage(detectBitmap, inputWidth, inputHeight)
        Log.i("MoveNet", "inputTensor: ${inputWidth}x${inputHeight}")
        val outputTensor = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32)
        Log.i("MoveNet", "outputTensor: ${outputShape[0]}x${outputShape[1]}x${outputShape[2]}")
        val widthRatio = detectBitmap.width.toFloat() / inputWidth
        val heightRatio = detectBitmap.height.toFloat() / inputHeight

        val positions = mutableListOf<Float>()

        inputTensor?.let { input ->
            Log.d("mean", "input1: ${input.tensorBuffer.floatArray[0]}")
            Log.d("mean", "input2: ${input.tensorBuffer.floatArray[inputWidth * inputHeight / 2]}")
            Log.d("mean", "input2: ${input.tensorBuffer.floatArray[inputWidth * inputHeight - 10]}")
            interpreter.run(input.buffer, outputTensor.buffer.rewind())
            val output = outputTensor.floatArray
            for (idx in 0 until numKeyPoints) {
                val x = output[idx * 3 + 1] * inputWidth * widthRatio
                val y = output[idx * 3 + 0] * inputHeight * heightRatio

                positions.add(x)
                positions.add(y)
                val score = output[idx * 3 + 2]
                keyPoints.add(
                    KeyPoint(
                        BodyPart.fromInt(idx),
                        PointF(x, y),
                        score
                    )
                )
                totalScore += score
            }
        }
        val matrix = Matrix()
        val points = positions.toFloatArray()

        matrix.postTranslate(0F, 0F)
        matrix.mapPoints(points)
        keyPoints.forEachIndexed { index, keyPoint ->
            keyPoint.coordinate =
                PointF(
                    points[index * 2],
                    points[index * 2 + 1]
                )
        }
        lastInferenceTimeNanos = SystemClock.elapsedRealtimeNanos() - inferenceStartTimeNanos
        Log.d("MoveNet", Person(keyPoints, totalScore / numKeyPoints).toString())
        return Person(keyPoints, totalScore / numKeyPoints)
    }

    private fun processInputImage(bitmap: Bitmap, inputWidth: Int, inputHeight: Int): TensorImage? {
        val width: Int = bitmap.width
        val height: Int = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var sumRed: Long = 0
        var sumGreen: Long = 0
        var sumBlue: Long = 0
        for (pixel in pixels) {
            // Get the red, green, and blue values of the pixel
            val red: Int = Color.red(pixel)
            val green: Int = Color.green(pixel)
            val blue: Int = Color.blue(pixel)

            // Add the red, green, and blue values to the sum
            sumRed += red.toLong()
            sumGreen += green.toLong()
            sumBlue += blue.toLong()
        }
        val meanRed = (sumRed / pixels.size).toDouble()
        val meanGreen = (sumGreen / pixels.size).toDouble()
        val meanBlue = (sumBlue / pixels.size).toDouble()
        Log.d("mean", "meanRed: $meanRed, meanGreen: $meanGreen, meanBlue: $meanBlue")
        Log.d("Bitmap", "pixel0: ${bitmap.getPixel(20, 20)}")
        Log.d("Bitmap", "pixel1: ${bitmap.getPixel(width / 2, height / 2)}")
        Log.d("Bitmap", "pixel2: ${bitmap.getPixel(width - 30, height-30)}")
        val size = if (height > width) width else height
        val imageProcessor = ImageProcessor.Builder().apply {
            add(ResizeWithCropOrPadOp(size, size))
            add(ResizeOp(inputWidth, inputHeight, ResizeOp.ResizeMethod.BILINEAR))
        }.build()
        val tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(bitmap)
        return imageProcessor.process(tensorImage)
    }

//    private fun processInputImage(intArray: IntArray, inputWidth: Int, inputHeight: Int): TensorImage? {
//        val width: Int = bitmap.width
//        val height: Int = bitmap.height
//        Log.d("Bitmap", "pixel0: ${bitmap.getPixel(20, 20)}")
//        Log.d("Bitmap", "pixel1: ${bitmap.getPixel(width / 2, height / 2)}")
//        Log.d("Bitmap", "pixel2: ${bitmap.getPixel(width - 30, height-30)}")
//        val size = if (height > width) width else height
//        val imageProcessor = ImageProcessor.Builder().apply {
//            add(ResizeWithCropOrPadOp(size, size))
//            add(ResizeOp(inputWidth, inputHeight, ResizeOp.ResizeMethod.BILINEAR))
//        }.build()
//        val tensorImage = TensorImage(DataType.UINT8)
//        tensorImage.load(intArray)
//        return imageProcessor.process(tensorImage)
//    }

    private fun initRectF(imageWidth: Int, imageHeight: Int): RectF {
        val xMin: Float
        val yMin: Float
        val width: Float
        val height: Float
        if (imageWidth > imageHeight) {
            width = 1f
            height = imageWidth.toFloat() / imageHeight
            xMin = 0f
            yMin = (imageHeight / 2f - imageWidth / 2f) / imageHeight
        } else {
            height = 1f
            width = imageHeight.toFloat() / imageWidth
            yMin = 0f
            xMin = (imageWidth / 2f - imageHeight / 2) / imageWidth
        }
        return RectF(
            xMin,
            yMin,
            xMin + width,
            yMin + height
        )
    }

    override fun lastInferenceTimeNanos(): Long = lastInferenceTimeNanos

    override fun close() {
        interpreter.close()
    }
}