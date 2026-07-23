package org.muslim_voice.project.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.muslim_voice.project.core.ui.theme.AppColors

enum class IconPosition {
    START,
    END
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.END,
    isTransparent: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    backgroundColor: Color = if (isTransparent) Color.Transparent else MaterialTheme.colorScheme.primary,
    contentColor: Color = if (isTransparent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
    cornerRadius: Dp = 12.dp,
    height: Dp = 50.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        elevation = if (isTransparent) ButtonDefaults.buttonElevation(0.dp, 0.dp) else ButtonDefaults.buttonElevation(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (icon != null && iconPosition == IconPosition.START) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = contentColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = text,
                        fontSize = 16.sp,
                        color = contentColor
                    )

                    if (icon != null && iconPosition == IconPosition.END) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = contentColor
                        )
                    }
                }
            }
        }
    }
}