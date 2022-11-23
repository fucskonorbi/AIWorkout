package hu.bme.aut.android.aiworkout.domain

import android.content.Context
import hu.bme.aut.android.aiworkout.data.Device
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil

enum class ModelType {
    Lightning,
    Thunder
}

//class MoveNet(private val interpreter: Interpreter): PoseDetector {
//
//    private val inputWidth = interpreter.getInputTensor(0).shape()[1]
//    private val inputHeight = interpreter.getInputTensor(0).shape()[2]
//    private var CPU_NUM_THREADS = 4
//
//    override fun close() {
//        interpreter.close()
//    }
//
//    fun create(context: Context, device: Device, modelType: ModelType): MoveNet {
//        val options = Interpreter.Options()
//        options.setNumThreads(CPU_NUM_THREADS)
//        var gpuDelegate: GpuDelegate? = null
//
//        when (device) {
//            Device.CPU -> {
//            }
//            Device.GPU -> {
//                gpuDelegate = GpuDelegate()
//                options.addDelegate(gpuDelegate)
//            }
//        }
//    }
//}