package hu.bme.aut.android.aiworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.android.aiworkout.presentation.ui.theme.AIWorkoutTheme
import hu.bme.aut.android.aiworkout.util.Navigation
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create FirebaseStorage instance and get reference to the storage
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val gsReference = storage.getReferenceFromUrl("gs://aiworkout-22b29.appspot.com/Firebase/ML/Models/pose_classifier.tflite")
        val localFile = File.createTempFile("pose_model", "tflite")
        gsReference.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
            // print the file path
            println(localFile.absolutePath)
        }.addOnFailureListener {
            // Handle any errors
        }

        setContent {
            AIWorkoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
    }
}
