package hu.bme.aut.android.aiworkout.data

data class Person(
    var id: Int = -1,
    val keyPoints: List<KeyPoint>,
    val score: Float
)
