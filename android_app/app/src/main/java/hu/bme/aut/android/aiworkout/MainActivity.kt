package hu.bme.aut.android.aiworkout

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.dataStore
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.aiworkout.ui.theme.AIWorkoutTheme
import hu.bme.aut.android.aiworkout.util.AppSettingsSerializer



@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIWorkoutTheme(darkTheme = true) {
                val navGraph = rememberNavController()
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
