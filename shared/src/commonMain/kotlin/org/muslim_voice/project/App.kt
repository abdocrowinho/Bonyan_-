package org.muslim_voice.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.muslim_voice.project.core.ui.theme.MuslimVoiceTheme
import org.muslim_voice.project.di.appModule
import org.muslim_voice.project.di.dataModule
import org.muslim_voice.project.di.domainModule
import org.muslim_voice.project.navigation.AppNavHost
import org.muslim_voice.project.navigation.AppNavigator

@Composable
fun App(platformModule: Module) {
    KoinApplication(application = {
        modules(appModule, domainModule, dataModule, platformModule)
    }) {
        MuslimVoiceTheme {
            val navigator: AppNavigator = koinInject()
            val navController = rememberNavController()
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavHost(navController = navController, navigator = navigator)
            }
        }
    }
}
