package org.muslim_voice.project.features.locationPermission.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.component.IconPosition
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.pickLocationScreen.event.LocationPermissionEvent
import org.muslim_voice.project.features.pickLocationScreen.intent.LocationPermissionIntent
import org.muslim_voice.project.features.pickLocationScreen.ui_model.PermissionFeatureItem
import org.muslim_voice.project.features.pickLocationScreen.viewModel.LocationPermissionViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@Composable
fun LocationPermissionScreen(
    navigator: AppNavigator,
    viewModel: LocationPermissionViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    // React to one-shot events coming from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                LocationPermissionEvent.RequestSystemLocationPermission -> {
                    scope.launch {
                        val status = ""
                        viewModel.handleIntent(LocationPermissionIntent.OnPermissionResult(status))
                    }
                }

                LocationPermissionEvent.NavigateToNextScreen -> {
                    navigator.navigateToOuter(Screens.Login, popUpTo = Screens.LocationPermissionScreen,
                        inclusive = true)
                }
            }
        }
    }

    // Skip this screen entirely if permission is already granted
    LaunchedEffect(Unit) {
        val status = ""

    }

    LocationPermissionContent(
        features = state.features,
        isLoading = state.isLoading,
        onAllowClicked = { viewModel.handleIntent(LocationPermissionIntent.OnAllowLocationClicked) },
        onSkipClicked = { viewModel.handleIntent(LocationPermissionIntent.OnSkipClicked) }
    )
}

@Composable
private fun LocationPermissionContent(
    features: List<PermissionFeatureItem>,
    isLoading: Boolean,
    onAllowClicked: () -> Unit,
    onSkipClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(AppColors.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = AppColors.OnPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "تحديد الموقع",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.OnSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "نحتاج إلى موقعك لحساب أوقات الصلاة الدقيقة في منطقتك وإظهار المساجد القريبة منك",
                    fontSize = 14.sp,
                    color = AppColors.Subtle,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    features.forEach { feature -> FeatureRow(feature) }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AppButton(
                    text = "السماح بالوصول للموقع",
                    onClick = onAllowClicked,
                    icon = Icons.Filled.LocationOn,
                    iconPosition = IconPosition.START,
                    isLoading = isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onSkipClicked) {
                    Text(text = "تخطي الآن", color = AppColors.Subtle, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "يمكنك تغيير هذا الإعداد لاحقًا من الإعدادات",
                    fontSize = 12.sp,
                    color = AppColors.Subtle,
                    textAlign = TextAlign.Center
                )
            }

    }
}

@Composable
private fun FeatureRow(feature: PermissionFeatureItem) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = AppColors.OnPrimary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBadge(icon = feature.icon)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(feature.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = AppColors.OnSurface)
                Text(feature.subtitle, fontSize = 12.sp, color = AppColors.Subtle)
            }
        }
    }
}

@Composable
private fun IconBadge(icon: ImageVector) {
    Box(
        modifier = Modifier.size(40.dp).clip(CircleShape).background(AppColors.PrimaryLight),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(20.dp))
    }
}