package org.muslim_voice.project.features.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.mvi.shouldShowOfflineBanner
import org.muslim_voice.project.core.ui.components.LoadingOverlay
import org.muslim_voice.project.core.ui.components.NoInternetBanner
import org.muslim_voice.project.features.mainHome.effect.MainHomeUiEffect
import org.muslim_voice.project.features.mainHome.state.MainHomeUiState
import org.muslim_voice.project.features.mainHome.viewModel.MainHomeViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.MainBottomNavBar
import org.muslim_voice.project.navigation.Screens

@Composable
fun MainHomeScreen(
    navigator: AppNavigator,
    viewModel: MainHomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is MainHomeUiEffect.NavigateToQibla ->
                    navigator.navigateTo(Screens.Qibla)
                is MainHomeUiEffect.NavigateToQuran ->
                    navigator.navigateTo(Screens.Quran)
                is MainHomeUiEffect.NavigateToGroups ->
                    navigator.navigateTo(Screens.Groups)
                is MainHomeUiEffect.NavigateToProfile ->
                    navigator.navigateTo(Screens.Profile)
                is MainHomeUiEffect.ShowError -> Unit
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                MainBottomNavBar(
                    selectedIndex = uiState.selectedTabIndex,
                    onTabSelected = { viewModel.onIntent(MainHomeIntent.SelectTab(it)) },
                )
            },
        ) { innerPadding ->
            when (uiState.selectedTabIndex) {
                0 -> HomeTabContent(
                    uiState = uiState,
                    onQiblaClick = { viewModel.onIntent(MainHomeIntent.NavigateToQibla) },
                    modifier = Modifier.padding(innerPadding),
                )
                1 -> PlaceholderTabContent(
                    title = "مصحفي",
                    modifier = Modifier.padding(innerPadding),
                )
                2 -> PlaceholderTabContent(
                    title = "مجموعات",
                    modifier = Modifier.padding(innerPadding),
                )
                3 -> PlaceholderTabContent(
                    title = "ملفي الشخصي",
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }

        if (uiState.shouldShowOfflineBanner()) {
            NoInternetBanner(
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }

        LoadingOverlay(visible = uiState.isLoading && uiState.prayerTimes == null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTabContent(
    uiState: MainHomeUiState,
    onQiblaClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "مسلم فويس",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    )
                    if (uiState.hijriDateLabel.isNotBlank()) {
                        Text(
                            text = uiState.hijriDateLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            if (uiState.gregorianDateLabel.isNotBlank()) {
                Text(
                    text = uiState.gregorianDateLabel,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            PrayerTimesCard(
                uiState = uiState,
                onQiblaClick = onQiblaClick,
            )
        }
    }
}

@Composable
private fun PlaceholderTabContent(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}
