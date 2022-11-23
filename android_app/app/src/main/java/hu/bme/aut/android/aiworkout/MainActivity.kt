package hu.bme.aut.android.aiworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import hu.bme.aut.android.aiworkout.presentation.home.HomeScreen
import hu.bme.aut.android.aiworkout.ui.AuthViewModel
import hu.bme.aut.android.aiworkout.ui.theme.AIWorkoutTheme
import hu.bme.aut.android.aiworkout.util.BottomBarScreen
import java.io.File


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            AIWorkoutTheme(darkTheme = true) {
                val navGraph = rememberNavController()
                DestinationsNavHost(navGraph = navGraphs.root)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Workouts,
        BottomBarScreen.Calendar
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
