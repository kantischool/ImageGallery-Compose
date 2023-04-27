package com.example.imagegallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavDrawer(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    val screens = listOf(
        DrawerScreens.Home,
        DrawerScreens.Search,
    )

    Column(
        modifier
            .fillMaxHeight().background(Color.White)
            .padding(start = 24.dp, top = 48.dp)
    ) {
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = screen.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.clickable {
                    onDestinationClicked(screen.route)
                }.padding(end = 10.dp)
            )
        }
    }
}