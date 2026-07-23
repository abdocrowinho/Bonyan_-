package org.muslim_voice.project.features.auth.register.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.component.AppDatePickerField
import org.muslim_voice.project.core.component.AppDropdownField
import org.muslim_voice.project.core.component.AppTextButton
import org.muslim_voice.project.core.component.AppTextField
import org.muslim_voice.project.core.media.rememberImagePickerController
import org.muslim_voice.project.core.microphone.rememberAudioRecorderController
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.auth.register.RegisterLaunchHolder
import org.muslim_voice.project.features.auth.register.event.RegisterEvent
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState
import org.muslim_voice.project.features.auth.register.ui_Model.PrayType
import org.muslim_voice.project.features.auth.register.ui_Model.RegisterStep
import org.muslim_voice.project.features.auth.register.ui_Model.arabicLabel
import org.muslim_voice.project.features.auth.register.viewModel.RegisterViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@Composable
fun RegisterScreen(
    navigator: AppNavigator,
    viewModel: RegisterViewModel = koinViewModel<RegisterViewModel>(),
    registerLaunchHolder: RegisterLaunchHolder = koinInject(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val audioRecorderController = rememberAudioRecorderController()
    val imagePickerController = rememberImagePickerController()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setGoogleAccount(registerLaunchHolder.consumeGoogleAccount())
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                RegisterEvent.RequestMicRecording -> {
                    val prayType = viewModel.consumeActiveRecordingType() ?: return@collect
                    val current = viewModel.state.value
                    scope.launch {
                        when {
                            current.currentlyRecording == prayType -> audioRecorderController.startRecording()
                            current.currentlyPlaying == prayType -> {
                                current.recordings[prayType]?.let { audioRecorderController.play(it) }
                            }
                            else -> {
                                val bytes = audioRecorderController.stopRecording()
                                if (bytes.isNotEmpty()) {
                                    viewModel.handleIntent(
                                        RegisterIntent.OnRecordingCaptured(prayType, bytes)
                                    )
                                }
                            }
                        }
                    }
                }

                RegisterEvent.RequestGalleryPick -> {
                    scope.launch {
                        imagePickerController.pickFromGallery()?.let { bytes ->
                            viewModel.handleIntent(RegisterIntent.OnProfileImageCaptured(bytes))
                        }
                    }
                }

                RegisterEvent.RequestCameraCapture -> {
                    scope.launch {
                        imagePickerController.captureFromCamera()?.let { bytes ->
                            viewModel.handleIntent(RegisterIntent.OnProfileImageCaptured(bytes))
                        }
                    }
                }

                RegisterEvent.NavigateToHome -> {
                    navigator.navigateToOuter(
                        route = Screens.MainHomeScreen,
                        popUpTo = Screens.Register,
                        inclusive = true,
                    )
                }

                is RegisterEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RegisterStepProgress(current = state.currentStep)
                Spacer(modifier = Modifier.height(24.dp))

                when (state.currentStep) {
                    RegisterStep.PERSONAL_INFO -> PersonalInfoStep(state, viewModel::handleIntent)
                    RegisterStep.PRAYER_VOICES -> PrayerVoicesStep(state, viewModel::handleIntent)
                    RegisterStep.PROFILE_PICTURE -> ProfilePictureStep(state, viewModel::handleIntent)
                    RegisterStep.VERIFICATION -> {
                        if (state.googleAccount == null) {
                            VerificationStep(state, viewModel::handleIntent)
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun RegisterStepProgress(
    current: RegisterStep,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RegisterStep.entries.forEach { step ->
            val isActive = step.ordinal <= current.ordinal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isActive) AppColors.Primary else AppColors.Divider),
            )
        }
    }
}

@Composable
private fun PersonalInfoStep(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "إنشاء الحساب",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AppColors.OnSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        AppTextField(
            value = state.firstName,
            onValueChange = { onIntent(RegisterIntent.OnFirstNameChanged(it)) },
            label = "الاسم الأول",
            errorMessage = state.step1Errors["firstName"],
        )
        AppTextField(
            value = state.lastName,
            onValueChange = { onIntent(RegisterIntent.OnLastNameChanged(it)) },
            label = "اسم العائلة",
            errorMessage = state.step1Errors["lastName"],
        )
        AppDatePickerField(
            selectedDate = state.birthDate,
            onDateSelected = { onIntent(RegisterIntent.OnBirthDateSelected(it)) },
            label = "تاريخ الميلاد",
            errorMessage = state.step1Errors["birthDate"],
        )
        AppTextField(
            value = state.roleModel,
            onValueChange = { onIntent(RegisterIntent.OnRoleModelChanged(it)) },
            label = "القدوة",
            errorMessage = state.step1Errors["roleModel"],
        )
        AppTextField(
            value = state.favoriteSurah,
            onValueChange = { onIntent(RegisterIntent.OnFavoriteSurahChanged(it)) },
            label = "السورة المفضلة",
            errorMessage = state.step1Errors["favoriteSurah"],
        )
        AppTextField(
            value = state.favoriteAyah,
            onValueChange = { onIntent(RegisterIntent.OnFavoriteAyahChanged(it)) },
            label = "الآية المفضلة",
            errorMessage = state.step1Errors["favoriteAyah"],
        )
        AppDropdownField(
            selected = state.selectedCountry,
            options = state.countryOptions,
            onOptionSelected = { onIntent(RegisterIntent.OnCountrySelected(it)) },
            label = "الدولة",
            searchable = true,
            errorMessage = state.step1Errors["country"],
        )

        AppButton(
            text = "التالي",
            onClick = { onIntent(RegisterIntent.OnStep1NextClicked) },
            isLoading = state.isLoading,
        )
    }
}

@Composable
private fun PrayerVoicesStep(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "ادخل الصوت الذي سيسمعه اصدقائك عند كل صلاة",
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.OnSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        PrayType.entries.forEach { prayType ->
            PrayerVoiceRow(
                prayType = prayType,
                hasRecording = state.recordings.containsKey(prayType),
                isRecording = state.currentlyRecording == prayType,
                isPlaying = state.currentlyPlaying == prayType,
                onIntent = onIntent,
            )
        }

        AppButton(
            text = "التالي",
            onClick = { onIntent(RegisterIntent.OnStep2NextClicked) },
            enabled = PrayType.entries.all { state.recordings.containsKey(it) },
        )
    }
}

@Composable
private fun PrayerVoiceRow(
    prayType: PrayType,
    hasRecording: Boolean,
    isRecording: Boolean,
    isPlaying: Boolean,
    onIntent: (RegisterIntent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AppColors.Surface,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = prayType.arabicLabel(),
                    color = AppColors.OnSurface,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = if (hasRecording) "تم تسجيل الصوت" else "لم يتم التسجيل بعد",
                    color = AppColors.Subtle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            IconButton(
                onClick = {
                    if (isRecording) {
                        onIntent(RegisterIntent.OnStopRecording(prayType))
                    } else {
                        onIntent(RegisterIntent.OnStartRecording(prayType))
                    }
                },
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                    contentDescription = null,
                    tint = if (isRecording) AppColors.Error else AppColors.Primary,
                )
            }

            IconButton(
                onClick = { onIntent(RegisterIntent.OnPlayRecording(prayType)) },
                enabled = hasRecording && !isPlaying,
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = if (hasRecording) AppColors.Primary else AppColors.Subtle,
                )
            }
        }
    }
}

@Composable
private fun ProfilePictureStep(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(132.dp)
                .clip(CircleShape)
                .background(AppColors.PrimaryLight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (state.profileImageBytes == null) Icons.Filled.Person else Icons.Filled.Check,
                contentDescription = null,
                tint = AppColors.Primary,
                modifier = Modifier.size(56.dp),
            )
        }

        AppButton(
            text = "من المعرض",
            onClick = { onIntent(RegisterIntent.OnPickFromGalleryClicked) },
            icon = Icons.Filled.PhotoLibrary,
            isTransparent = true,
        )

        AppButton(
            text = "من الكاميرا",
            onClick = { onIntent(RegisterIntent.OnCaptureFromCameraClicked) },
            icon = Icons.Filled.CameraAlt,
            isTransparent = true,
        )

        AppButton(
            text = "التالي",
            onClick = { onIntent(RegisterIntent.OnStep3NextClicked) },
            enabled = state.profileImageBytes != null,
            isLoading = state.isLoading,
        )
    }
}

@Composable
private fun VerificationStep(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppTextField(
            value = state.verificationCode,
            onValueChange = { onIntent(RegisterIntent.OnVerificationCodeChanged(it)) },
            label = "رمز التحقق",
            keyboardType = KeyboardType.Number,
            errorMessage = state.verificationError,
        )

        AppButton(
            text = "تأكيد",
            onClick = { onIntent(RegisterIntent.OnVerifyClicked) },
            isLoading = state.isLoading,
        )

        AppTextButton(
            text = "إعادة إرسال الكود",
            onClick = { onIntent(RegisterIntent.OnResendCodeClicked) },
            enabled = !state.isResendingCode,
        )
    }
}
