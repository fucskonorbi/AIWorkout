package hu.bme.aut.android.aiworkout.ui.views.settings

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.android.aiworkout.ui.views.sign_in.SignInViewModel
import hu.bme.aut.android.aiworkout.util.AppSettings
import hu.bme.aut.android.aiworkout.util.AppSettingsSerializer

val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)

@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Model type",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(10.dp)
                .border(1.dp, color = androidx.compose.ui.graphics.Color.Black)
                .clip(RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = true,
                onClick = {  }
            )
            Text(
                text = "Lightning",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            RadioButton(
                selected = false,
                onClick = {  }
            )
            Text(
                text = "Thunder",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Classification type",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(10.dp)
                .border(1.dp, color = androidx.compose.ui.graphics.Color.Black)
                .clip(RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = true,
                onClick = {  }
            )
            Text(
                text = "Exercises",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            RadioButton(
                selected = false,
                onClick = {  }
            )
            Text(
                text = "Yoga",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(10.dp)
                .border(1.dp, color = androidx.compose.ui.graphics.Color.Black)
                .clip(RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Show prediction scores",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Switch(
                checked = true,
                onCheckedChange = {  }
            )
        }
       Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(10.dp)
                .border(1.dp, color = androidx.compose.ui.graphics.Color.Black)
                .clip(RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Draw on canvas",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Switch(
                checked = true,
                onCheckedChange = {  },
            )
        }


    }

}