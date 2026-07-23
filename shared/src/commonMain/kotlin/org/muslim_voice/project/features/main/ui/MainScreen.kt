package org.muslim_voice.project.features.main.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.InnerNavHost

@Composable
fun MainScreen(navigator: AppNavigator) {
    val innerNavController = rememberNavController()
    InnerNavHost(
        navController = innerNavController,
        navigator = navigator,
    )
}
