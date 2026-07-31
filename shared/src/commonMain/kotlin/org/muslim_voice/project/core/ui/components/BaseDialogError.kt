package org.muslim_voice.project.core.ui.components


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import org.muslim_voice.project.core.ui.theme.AppColors


@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {

    var visible by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(Unit) {

        visible = true

        delay(3000)

        visible = false

        delay(300)

        onDismiss()
    }


    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(),

        exit = scaleOut(
            animationSpec = tween(250)
        ) + fadeOut()
    ) {


        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {


            ErrorDialogContent(
                message = message
            )

        }
    }
}



@Composable
private fun ErrorDialogContent(
    message: String
) {


    val infiniteTransition =
        rememberInfiniteTransition()


    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        )
    )


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),

        shape = RoundedCornerShape(28.dp),

        color = AppColors.Surface,

        shadowElevation = 12.dp
    ) {


        Column(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(
                modifier = Modifier
                    .size(72.dp)
                    .scale(pulseScale)
                    .background(
                        color = AppColors.Error.copy(
                            alpha = 0.12f
                        ),
                        shape = CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {


                Icon(
                    imageVector =
                        Icons.Default.ErrorOutline,

                    contentDescription = null,

                    tint = AppColors.Error,

                    modifier = Modifier.size(42.dp)
                )

            }



            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Text(
                text = "Oops!",
                style = MaterialTheme.typography.titleLarge,

                color = AppColors.Error
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Text(
                text = message,

                style = MaterialTheme.typography.bodyMedium,

                color = AppColors.OnSurface,

                modifier = Modifier.fillMaxWidth(),

                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )



            Spacer(
                modifier = Modifier.height(18.dp)
            )


            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),

                color = AppColors.Error,

                trackColor = AppColors.Error.copy(
                    alpha = 0.15f
                )
            )

        }
    }
}