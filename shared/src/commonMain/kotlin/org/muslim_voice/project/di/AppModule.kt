package org.muslim_voice.project.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.muslim_voice.project.core.auth.GoogleSignInController
import org.muslim_voice.project.core.auth.createGoogleSignInController
import org.muslim_voice.project.core.data.UserPreferencesRepository
import org.muslim_voice.project.core.data.repository.AuthRepositoryImpl
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.features.auth.login.viewModel.LoginViewModel
import org.muslim_voice.project.features.auth.register.RegisterLaunchHolder
import org.muslim_voice.project.features.auth.register.viewModel.RegisterViewModel
import org.muslim_voice.project.features.groupDashboard.viewModel.GroupDashboardViewModel
import org.muslim_voice.project.features.mainHome.viewModel.MainHomeViewModel
import org.muslim_voice.project.features.notificationsHistory.viewModel.NotificationsHistoryViewModel
import org.muslim_voice.project.features.onboarding.viewModel.OnboardingViewModel
import org.muslim_voice.project.features.pickLocationScreen.viewModel.LocationPermissionViewModel
import org.muslim_voice.project.features.selectLanguage.viewModel.SelectLanViewModel
import org.muslim_voice.project.features.splash.viewModel.SplashViewModel
import org.muslim_voice.project.navigation.AppNavigator

val appModule = module {
    includes(dataModule)
    single { AppNavigator() }
    single { UserPreferencesRepository() }
    single<AuthRepository> { AuthRepositoryImpl() }
    single<GoogleSignInController> { createGoogleSignInController() }
    single { RegisterLaunchHolder() }
    viewModel { SplashViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { GroupDashboardViewModel() }
    viewModel { NotificationsHistoryViewModel() }
    viewModel { SelectLanViewModel() }
    viewModel { LocationPermissionViewModel() }
    viewModel { MainHomeViewModel(get()) }
}
