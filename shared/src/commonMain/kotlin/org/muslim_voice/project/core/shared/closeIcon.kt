package org.muslim_voice.project.core.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.muslim_voice.project.core.utiltis.sdp

@Composable
fun closeIcon(
    onClose: () -> Unit = {},
){

    Box(
        modifier = Modifier
            .size(24.sdp) // width & height: 24px
            .clickable { onClose() }.background(
                color = MaterialTheme.colorScheme.primary.copy(.01f),
            ).border(width = .5.sdp,
                shape = RoundedCornerShape(999.sdp), color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.primary, // background: #00AE6B
            modifier = Modifier.size(18.sdp)
        )
    }

}