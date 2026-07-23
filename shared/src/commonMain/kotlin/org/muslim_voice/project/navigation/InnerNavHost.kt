package org.muslim_voice.project.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.jetbrains.compose.resources.stringResource
import org.muslim_voice.project.generated.resources.*
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.features.groupDashboard.ui.GroupDashboardScreen
import org.muslim_voice.project.features.notificationsHistory.ui.NotificationsHistoryScreen

@Composable
fun InnerNavHost(
    navController: NavHostController,
    navigator: AppNavigator,
) {
    LaunchedEffect(Unit) {
        navigator.innerNavEvents.collect { intent ->
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
        startDestination = Screens.GroupDashboard,
    ) {
        composable<Screens.GroupDashboard> {
            GroupDashboardScreen(navigator = navigator)
        }
        composable<Screens.NotificationsHistory> {
            NotificationsHistoryScreen(navigator = navigator)
        }
        composable<Screens.WalkieTalkie> {
            PlaceholderScreen(title = stringResource(Res.string.placeholder_walkie))
        }
        composable<Screens.Library> {
            PlaceholderScreen(title = stringResource(Res.string.placeholder_library))
        }
        composable<Screens.Stories> {
            PlaceholderScreen(title = stringResource(Res.string.placeholder_stories))
        }

        composable<Screens.Qibla> {
            PlaceholderScreen(title = "القبلة")
        }
        composable<Screens.Quran> {
            PlaceholderScreen(title = "مصحفي")
        }
        composable<Screens.Groups> {
            PlaceholderScreen(title = "مجموعات")
        }
        composable<Screens.Profile> {
            PlaceholderScreen(title = "ملفي الشخصي")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    AppBackground {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = title)
        }
    }
}
