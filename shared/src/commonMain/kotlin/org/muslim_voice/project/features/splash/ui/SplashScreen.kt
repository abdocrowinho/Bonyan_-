package org.muslim_voice.project.features.splash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.utiltis.ssp
import org.muslim_voice.project.features.splash.viewModel.SplashViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@Composable
fun SplashScreen(
    navigator: AppNavigator,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.effect.collect { effect ->
//            when (effect) {
//                SplashEffect.NavigateToOnboarding -> {
//                }
//                SplashEffect.NavigateToMain -> {}
//            }
//        }
//    }

    LaunchedEffect(Unit){
        delay(2000)
        navigator.navigateToOuter(route = Screens.Onboarding, inclusive = true, popUpTo = Screens.Splash)

    }

    val figmaGradient = Brush.verticalGradient(
        0.0f to Color(0xff0D7E5E),   // 0%
        0.5f to Color(0xff0D7E5E),   // 50%
        1.0f to Color(0xff0A6349)    // 100%
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = figmaGradient),
    ) {

        Text(
            text = "بُنيان",
            fontSize = 36.ssp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )


    }
}