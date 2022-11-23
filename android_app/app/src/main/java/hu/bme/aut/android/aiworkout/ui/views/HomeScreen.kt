package hu.bme.aut.android.aiworkout.presentation.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().background(Color(0xFF6200EE))
    ){
    }
}

@Composable
fun FeaturedCard(
    card_name : String,
    card_icon: ImageVector,
    navigateTo: String,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Surface(
        modifier = modifier.fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate(navigateTo) },
        color = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.medium,
        elevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                text = card_name,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
                    .align(Alignment.Start)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                imageVector = card_icon,
                contentDescription = "Card Icon",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.End)
            )
        }
    }
}