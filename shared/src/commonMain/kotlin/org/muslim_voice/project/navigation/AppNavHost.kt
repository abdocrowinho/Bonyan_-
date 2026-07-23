package org.muslim_voice.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.muslim_voice.project.features.auth.login.ui.LoginScreen
import org.muslim_voice.project.features.auth.register.ui.RegisterScreen
import org.muslim_voice.project.features.locationPermission.ui.LocationPermissionScreen
import org.muslim_voice.project.features.main.ui.MainHomeScreen
import org.muslim_voice.project.features.main.ui.MainScreen
import org.muslim_voice.project.features.onboarding.ui.OnboardingScreen
import org.muslim_voice.project.features.selectLanguage.SelectLanScreen
import org.muslim_voice.project.features.splash.ui.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    navigator: AppNavigator,
) {
    LaunchedEffect(Unit) {
        navigator.navEvents.collect { intent ->
            when (intent) {
                is NavigationIntent.Back -> navController.popBackStack()
                is NavigationIntent.To -> {
                    navController.navigate(intent.route) {
                        intent.popUpTo?.let { target ->
                            popUpTo(target) { inclusive = intent.inclusive }
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.Splash,
    ) {
        composable<Screens.Splash> {
            SplashScreen(navigator = navigator)
        }

        composable<Screens.Onboarding> {
            OnboardingScreen(navigator = navigator)
        }
        composable<Screens.Login> {
            LoginScreen(navigator = navigator)
        }
        composable<Screens.Register> {
            RegisterScreen(navigator = navigator)
        }
        composable<Screens.MainHomeScreen> {
            MainHomeScreen(navigator = navigator)
        }
        composable<Screens.SelectLan> {
            SelectLanScreen(navigator = navigator)
        }
        composable<Screens.LocationPermissionScreen> {
            LocationPermissionScreen(navigator = navigator)
        }
    }
}
