package org.muslim_voice.project.features.auth.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.auth.rememberGoogleSignInController
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.component.AppTextButton
import org.muslim_voice.project.core.component.AppTextField
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.auth.login.event.LoginEvent
import org.muslim_voice.project.features.auth.login.intent.LoginIntent
import org.muslim_voice.project.features.auth.login.viewModel.LoginViewModel
import org.muslim_voice.project.features.auth.register.RegisterLaunchHolder
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens
import org.koin.compose.koinInject
import org.muslim_voice.project.core.domain.validation.login.LoginFieldsConstants
import org.muslim_voice.project.core.ui.components.ErrorDialog

@Composable
fun LoginScreen(
    navigator: AppNavigator,
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>(),
    registerLaunchHolder: RegisterLaunchHolder = koinInject(),
) {

    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }
    var validationsErrors by remember { mutableStateOf<Map<String, List<String>?>?>(null) }

    rememberGoogleSignInController()


    LaunchedEffect(Unit) {

        viewModel.event.collect { event ->

            when (event) {

                LoginEvent.NavigateToHome -> {

                    navigator.navigateToOuter(
                        route = Screens.MainHomeScreen,
                        popUpTo = Screens.Login,
                        inclusive = true,
                    )
                }


                LoginEvent.NavigateToRegister -> {

                    registerLaunchHolder.setGoogleAccount(null)

                    navigator.navigateToOuter(
                        Screens.Register
                    )
                }


                is LoginEvent.NavigateToRegisterWithGoogleAccount -> {

                    registerLaunchHolder.setGoogleAccount(
                        event.account
                    )

                    navigator.navigateToOuter(
                        Screens.Register
                    )
                }


                is LoginEvent.ShowError -> {

                    errorMessage = event.message
                }

                is LoginEvent.ValidationError -> {
                    validationsErrors = event.errors
                }
            }
        }
    }



    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center,
        ) {


            Text(
                text = "تسجيل الدخول",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.OnSurface,
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            AppTextField(
                value = state.email,

                onValueChange = {
                    viewModel.handleIntent(
                        LoginIntent.OnEmailChanged(it)
                    )
                },

                label = "البريد الإلكتروني",

                errorMessage = validationsErrors?.get(LoginFieldsConstants.EMAIL)?.firstOrNull(),

                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            AppTextField(
                value = state.password,

                onValueChange = {
                    viewModel.handleIntent(
                        LoginIntent.OnPasswordChanged(it)
                    )
                },

                label = "كلمة المرور",

                errorMessage =validationsErrors?.get(LoginFieldsConstants.PASSWORD)?.firstOrNull(),

                isPassword = true,
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            AppButton(
                text = "تسجيل الدخول",

                onClick = {
                    viewModel.handleIntent(
                        LoginIntent.OnLoginClicked
                    )
                },

                isLoading = state.isLoading,
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment = Alignment.CenterVertically,
            ) {

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = AppColors.Divider
                )


                Text(
                    text = "أو",

                    modifier = Modifier.padding(horizontal = 12.dp),

                    color = AppColors.Subtle,

                    textAlign = TextAlign.Center,
                )


                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = AppColors.Divider
                )
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            AppButton(
                text = "تسجيل الدخول عبر Google",

                onClick = {
                    viewModel.handleIntent(
                        LoginIntent.OnGoogleSignInClicked
                    )
                },

                isTransparent = true,

                isLoading = state.isGoogleLoading,
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            AppTextButton(
                text = "إنشاء حساب جديد",

                onClick = {
                    viewModel.handleIntent(
                        LoginIntent.OnNavigateToRegisterClicked
                    )
                },
            )


            SnackbarHost(
                hostState = snackbarHostState
            )
        }


        errorMessage?.let { message ->

            ErrorDialog(
                message = message,

                onDismiss = {
                    errorMessage = null
                }
            )
        }
    }
}
