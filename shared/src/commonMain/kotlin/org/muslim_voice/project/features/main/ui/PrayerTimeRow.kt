package org.muslim_voice.project.features.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.muslim_voice.project.features.mainHome.state.PrayerStatus
import org.muslim_voice.project.features.mainHome.state.PrayerTimeItem

@Composable
fun PrayerTimeRow(
    item: PrayerTimeItem,
    modifier: Modifier = Modifier,
) {
    val isCurrentPrayer = item.status == PrayerStatus.CURRENT

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isCurrentPrayer) Modifier.background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.medium,
                ) else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PrayerStatusIndicator(status = item.status)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isCurrentPrayer) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrentPrayer)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
        Text(
            text = item.time,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = if (isCurrentPrayer)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isCurrentPrayer) FontWeight.SemiBold else FontWeight.Normal,
            ),
        )
    }
}

@Composable
private fun PrayerStatusIndicator(
    status: PrayerStatus,
    modifier: Modifier = Modifier,
) {
    when (status) {
        PrayerStatus.PASSED -> Box(
            modifier = modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(14.dp),
            )
        }

        PrayerStatus.CURRENT -> Box(
            modifier = modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary),
            )
        }

        PrayerStatus.UPCOMING -> Box(
            modifier = modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
    }
}
