package org.muslim_voice.project.features.pickLocationScreen.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Shield
import org.muslim_voice.project.features.pickLocationScreen.ui_model.PermissionFeatureItem

data class LocationPermissionState(
    val isLoading: Boolean = false,
    val features: List<PermissionFeatureItem> = listOf(
        PermissionFeatureItem(
            icon = Icons.Filled.LocationOn,
            title = "أوقات الصلاة الدقيقة",
            subtitle = "حساب دقيق بناء على موقعك"
        ),
        PermissionFeatureItem(
            icon = Icons.Filled.Navigation,
            title = "المساجد القريبة",
            subtitle = "اكتشاف المساجد من حولك"
        ),
        PermissionFeatureItem(
            icon = Icons.Filled.Shield,
            title = "خصوصية محمية",
            subtitle = "بياناتك آمنة ومحمية"
        )
    )
)