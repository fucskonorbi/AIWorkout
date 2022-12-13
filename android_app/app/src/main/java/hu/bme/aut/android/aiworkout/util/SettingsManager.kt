package hu.bme.aut.android.aiworkout.util

import kotlinx.serialization.Serializable
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


@Serializable
data class AppSettings(
    val drawOnScreen: Boolean = false,
    val showPredictionScores: Boolean = false,
    val modelType: ModelType = ModelType.LIGHTNING,
    val classificationType: ClassificationType = ClassificationType.EXERCISE,
)

enum class ModelType {
    LIGHTNING, THUNDER
}

enum class ClassificationType{
    YOGA, EXERCISE
}

@Suppress("BlockingMethodInNonBlockingContext")
object AppSettingsSerializer: Serializer<AppSettings>{
    override val defaultValue: AppSettings
        get() = AppSettings()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }


}