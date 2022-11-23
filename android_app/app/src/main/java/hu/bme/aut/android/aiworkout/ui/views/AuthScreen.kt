package hu.bme.aut.android.aiworkout.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.ui.theme.AIWorkoutTheme

@Destination(start=true)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator
) {
    var (email, _) = remember { mutableStateOf("") }
    var (password, _) = remember { mutableStateOf("") }
    AIWorkoutTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = MaterialTheme.colors.background)
                .border(1.dp, MaterialTheme.colors.onBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onBackground,
                fontSize = 24.sp,

                fontWeight = FontWeight.Bold,
                textAlign = MaterialTheme.typography.h4.textAlign,
                modifier = Modifier.padding(16.dp)
            )

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it })
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it })


                }
            }

        }

    }
}
