package org.muslim_voice.project.features.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.muslim_voice.project.features.mainHome.state.MainHomeUiState
import org.muslim_voice.project.features.mainHome.state.PrayerTimeItem

@Composable
fun PrayerTimesCard(
    uiState: MainHomeUiState,
    onQiblaClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CurrentPrayerHeader(
            currentPrayerName = uiState.currentPrayerName,
            currentTime = uiState.currentTime,
            remainingTime = uiState.remainingTime,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrayerListCard(prayerTimes = uiState.prayerTimeItems)
        Spacer(modifier = Modifier.height(12.dp))
        QiblaCard(onClick = onQiblaClick)
    }
}

@Composable
private fun CurrentPrayerHeader(
    currentPrayerName: String,
    currentTime: String,
    remainingTime: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "وقت الصلاة الحالي",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                ),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentPrayerName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentTime,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 48.sp,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = remainingTime,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                ),
            )
        }
    }
}

@Composable
private fun PrayerListCard(
    prayerTimes: List<PrayerTimeItem>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            prayerTimes.forEachIndexed { index, item ->
                PrayerTimeRow(item = item)
                if (index < prayerTimes.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun QiblaCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "اتجاه القبلة",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = "اضغط لفتح البوصلة",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    ),
                )
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "القبلة",
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = "القبلة",
                    modifier = Modifier.padding(start = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
