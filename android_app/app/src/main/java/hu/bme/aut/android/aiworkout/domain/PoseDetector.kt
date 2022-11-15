package hu.bme.aut.android.aiworkout.domain

import android.graphics.Bitmap
import hu.bme.aut.android.aiworkout.data.Person

interface PoseDetector : AutoCloseable {

    fun estimatePoses(bitmap: Bitmap): Person

    fun lastInferenceTimeNanos(): Long
}