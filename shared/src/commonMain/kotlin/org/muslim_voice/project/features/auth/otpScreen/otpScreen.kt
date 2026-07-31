package org.muslim_voice.project.features.auth.otpScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.component.AppTextField
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@Composable
fun OtpScreen(
    email: String,
    navigator: AppNavigator,
    viewModel: OtpViewModel = koinViewModel<OtpViewModel>(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(email) {
        viewModel.setEmail(email)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                OtpEvent.NavigateToLogin -> {
                    navigator.navigateToOuter(
                        route = Screens.Login,
                        popUpTo = Screens.Otp(email),
                        inclusive = true,
                    )
                }
            }
        }
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Verify OTP",
                style = MaterialTheme.typography.headlineSmall,
                color = AppColors.OnSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = state.otpCode,
                onValueChange = viewModel::onOtpChanged,
                label = "OTP code",
                keyboardType = KeyboardType.Number,
                errorMessage = state.otpError,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = "Confirm",
                onClick = viewModel::verifyOtp,
                isLoading = state.isLoading,
            )

            SnackbarHost(hostState = snackbarHostState)
        }
    }

    if (state.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissSuccessDialog,
            confirmButton = {
                TextButton(onClick = viewModel::dismissSuccessDialog) {
                    Text("OK")
                }
            },
            title = { Text("Success") },
            text = { Text("Your account has been verified.") },
        )
    }
}
