package org.muslim_voice.project.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppShapes

@Composable
fun HadithCard(
    quote: String,
    source: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.AccentLight, AppShapes.Medium)
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .background(AppColors.Accent, AppShapes.Small),
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = quote, style = MaterialTheme.typography.bodyLarge, color = AppColors.OnSurface)
            Text(
                text = source,
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.Subtle,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
