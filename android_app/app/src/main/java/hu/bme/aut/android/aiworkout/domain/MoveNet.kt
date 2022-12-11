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

        val numKeyPoints = outputShape[2]
        val keyPoints = mutableListOf<KeyPoint>()

        val detectBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);

        val inputTensor = processInputImage(detectBitmap, inputWidth, inputHeight)
        val outputTensor = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32)
        val widthRatio = detectBitmap.width.toFloat() / inputWidth
        val heightRatio = detectBitmap.height.toFloat() / inputHeight

        val positions = mutableListOf<Float>()

        inputTensor?.let { input ->
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
        return Person(keyPoints, totalScore / numKeyPoints)
    }

    private fun processInputImage(bitmap: Bitmap, inputWidth: Int, inputHeight: Int): TensorImage? {
        val width: Int = bitmap.width
        val height: Int = bitmap.height
        val size = if (height > width) width else height
        val imageProcessor = ImageProcessor.Builder().apply {
            add(ResizeWithCropOrPadOp(size, size))
            add(ResizeOp(inputWidth, inputHeight, ResizeOp.ResizeMethod.BILINEAR))
        }.build()
        val tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(bitmap)
        return imageProcessor.process(tensorImage)
    }

    override fun lastInferenceTimeNanos(): Long = lastInferenceTimeNanos

    override fun close() {
        interpreter.close()
    }
}