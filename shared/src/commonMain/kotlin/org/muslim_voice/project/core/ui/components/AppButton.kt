//package org.muslim_voice.project.core.ui.components
//
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.unit.dp
//import org.muslim_voice.project.core.ui.theme.AppColors
//import org.muslim_voice.project.core.ui.theme.AppShapes
//
//enum class AppButtonVariant { Primary, Outlined }
//
//@Composable
//fun AppButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    variant: AppButtonVariant = AppButtonVariant.Primary,
//    enabled: Boolean = true,
//    loading: Boolean = false,
//) {
//    val alphaModifier = if (enabled && !loading) Modifier else Modifier.alpha(0.4f)
//    val content: @Composable () -> Unit = {
//        if (loading) {
//            CircularProgressIndicator(
//                modifier = Modifier.size(22.dp),
//                color = if (variant == AppButtonVariant.Primary) AppColors.OnPrimary else AppColors.Primary,
//                strokeWidth = 2.dp,
//            )
//        } else {
//            val textColor = if (variant == AppButtonVariant.Primary) AppColors.OnPrimary else AppColors.Primary
//            Text(text = text, color = textColor)
//        }
//    }
//
//    when (variant) {
//        AppButtonVariant.Primary -> Button(
//            onClick = onClick,
//            enabled = enabled && !loading,
//            modifier = modifier.then(alphaModifier).fillMaxWidth().height(52.dp),
//            shape = AppShapes.Medium,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = AppColors.Primary,
//                contentColor = AppColors.OnPrimary,
//                disabledContainerColor = AppColors.Primary,
//                disabledContentColor = AppColors.OnPrimary,
//            ),
//            content = { content() },
//        )
//        AppButtonVariant.Outlined -> OutlinedButton(
//            onClick = onClick,
//            enabled = enabled && !loading,
//            modifier = modifier.then(alphaModifier).fillMaxWidth().height(52.dp),
//            shape = AppShapes.Medium,
//            colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Primary),
//            content = { content() },
//        )
//    }
//}
