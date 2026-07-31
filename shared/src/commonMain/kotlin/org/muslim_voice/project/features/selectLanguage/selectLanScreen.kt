package org.muslim_voice.project.features.selectLanguage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.core.utilities.sdp
import org.muslim_voice.project.core.utilities.ssp
import org.muslim_voice.project.features.selectLanguage.intent.SelectLanIntent
import org.muslim_voice.project.features.selectLanguage.ui_Model.LanItem
import org.muslim_voice.project.features.selectLanguage.viewModel.SelectLanViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@Composable
fun SelectLanScreen(
    navigator: AppNavigator,
    viewModel: SelectLanViewModel = koinViewModel<SelectLanViewModel>()
) {
    val state by viewModel.state.collectAsState()
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( start = 24.sdp, end = 24.sdp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.sdp)
                    .clip(shape = RoundedCornerShape(20.sdp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .border(
                        width = 1.sdp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.sdp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "language",
                    tint = Color.White,
                    modifier = Modifier.size(48.sdp),
                )
            }

            Spacer(modifier = Modifier.size(24.sdp))

            Text(
                "أختر اللغة",
                color = Color.Black,
                fontSize = 30.ssp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(8.sdp))
            Text(
                "Select Your Preferred Language",
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                fontSize = 16.ssp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.size(20.sdp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.sdp)
            ) {
                state.languages.forEach { item ->
                    LanItemRow(item) {
                        viewModel.handleIntent(SelectLanIntent.OnLanSelect(lan = item))
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.sdp))

            AppButton(onClick = {
                navigator.navigateToOuter(
                    route = Screens.LocationPermissionScreen,
                    popUpTo = Screens.SelectLan ,
                    inclusive = false
                )
            },
                text = "متابعة",
                modifier = Modifier.fillMaxWidth()
                )


        }
    }
}

@Composable
fun LanItemRow(
    lanItem: LanItem,
    onSelect: () -> Unit
) {
    val borderWidth = if (lanItem.isSelected) 1.5.sdp else 1.sdp
    val borderColor = if (lanItem.isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.LightGray.copy(alpha = 0.4f)
    }
    val backgroundColor = if (lanItem.isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        Color.White
    }

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.sdp)) // clip BEFORE background here too
            .background(color = backgroundColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(16.sdp)
            )
            .clickable { onSelect() }
            .height(90.sdp)
            .padding(horizontal = 16.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(lanItem.lanIcon, fontSize = 25.ssp, modifier = Modifier.padding(horizontal = 8.sdp))

        Column(verticalArrangement = Arrangement.spacedBy(2.sdp)) {
            Text(
                lanItem.lan,
                fontSize = 20.ssp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                lanItem.lanInLanSyntax,
                fontSize = 12.ssp,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray
            )
        }

        Spacer(Modifier.weight(1f))

        if (lanItem.isSelected) {
            Box(
                modifier = Modifier
                    .size(28.sdp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.sdp)
                )
            }
        }
    }
}