package hu.bme.aut.android.aiworkout.domain

import hu.bme.aut.android.aiworkout.data.Person
import org.tensorflow.lite.Interpreter

class PoseClassifier(
    private val interpreter: Interpreter,
    private val labels: List<String>
) {

    private val input = interpreter.getInputTensor(0).shape()
    private val output = interpreter.getOutputTensor(0).shape()

    fun classify(person: Person?): List<Pair<String, Float>>{
        val inputVector = FloatArray(input[1])
        val outputVector = FloatArray(output[1])

        person?.keyPoints?.forEachIndexed { index, keyPoint ->
            inputVector[index * 3] = keyPoint.coordinate.y
            inputVector[index * 3 + 1] = keyPoint.coordinate.x
            inputVector[index * 3 + 2] = keyPoint.score
        }

        interpreter.run(arrayOf(inputVector), arrayOf(outputVector))
        val output = mutableListOf<Pair<String, Float>>()
        outputVector.forEachIndexed { index, score ->
            output.add(Pair(labels[index], score))
        }
        return output
    }

    fun close() {
        interpreter.close()
    }
}