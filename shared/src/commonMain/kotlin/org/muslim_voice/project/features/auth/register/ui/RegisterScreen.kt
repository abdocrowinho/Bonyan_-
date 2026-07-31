package org.muslim_voice.project.features.auth.register.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.microphone.RecordingManger
import org.muslim_voice.project.core.microphone.RecordingStatus
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.features.auth.register.RegisterLaunchHolder
import org.muslim_voice.project.features.auth.register.component.PersonalInfoStep
import org.muslim_voice.project.features.auth.register.component.ProfilePictureStep
import org.muslim_voice.project.features.auth.register.component.RegisterStepProgress
import org.muslim_voice.project.features.auth.register.component.prayerVoice.PrayerVoicesStep
import org.muslim_voice.project.features.auth.register.event.RegisterEvent
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState
import org.muslim_voice.project.features.auth.register.ui_Model.RegisterStep
import org.muslim_voice.project.features.auth.register.viewModel.RegisterViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens

@Composable
fun RegisterScreen(
    navigator: AppNavigator,
    viewModel: RegisterViewModel = koinViewModel<RegisterViewModel>(),
    registerLaunchHolder: RegisterLaunchHolder = koinInject(),
    recordingManager: RecordingManger = koinInject(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        viewModel.setGoogleAccount(registerLaunchHolder.consumeGoogleAccount())
    }

    LaunchedEffect(recordingManager) {
        var lastStatus = RecordingStatus.IDLE
        recordingManager.getStatus().collect { status ->
            if (lastStatus == RecordingStatus.PLAYING && status == RecordingStatus.RECORDED) {
                viewModel.state.value.currentlyPlaying?.let { prayType ->
                    viewModel.handleIntent(RegisterIntent.OnPlaybackFinished(prayType))
                }
            }
            lastStatus = status
        }
    }

    LaunchedEffect(recordingManager) {
        viewModel.event.collect { event ->
            when (event) {
                is RegisterEvent.StartPrayerRecording -> {
                    scope.launch {
                        runCatching {
                            if (event.playbackToStop != null) {
                                recordingManager.stopPlayback()
                            }
                            event.recordingToStop?.let { stoppedPrayType ->
                                val recorded = recordingManager.stopRecording()
                                if (recorded.filePath.isNotBlank()) {
                                    viewModel.handleIntent(
                                        RegisterIntent.OnRecordingCaptured(
                                            prayType = stoppedPrayType,
                                            audioPath = recorded.filePath,
                                            durationMillis = recorded.durationMillis,
                                        )
                                    )
                                }
                            }
                            if (!recordingManager.hasPermission() && !recordingManager.requestPermission()) {
                                error("لم يتم منح إذن الميكروفون")
                            }
                            recordingManager.startRecording()
                        }.onFailure { error ->
                            viewModel.handleIntent(
                                RegisterIntent.OnPrayerAudioError(
                                    error.message ?: "تعذر بدء التسجيل"
                                )
                            )
                        }
                    }
                }

                is RegisterEvent.StopPrayerRecording -> {
                    scope.launch {
                        runCatching {
                            recordingManager.stopRecording()
                        }.onSuccess { recorded ->
                            if (recorded.filePath.isNotBlank()) {
                                viewModel.handleIntent(
                                    RegisterIntent.OnRecordingCaptured(
                                        prayType = event.prayType,
                                        audioPath = recorded.filePath,
                                        durationMillis = recorded.durationMillis,
                                    )
                                )
                            } else {
                                viewModel.handleIntent(
                                    RegisterIntent.OnPrayerAudioError("لم يتم حفظ التسجيل")
                                )
                            }
                        }.onFailure { error ->
                            viewModel.handleIntent(
                                RegisterIntent.OnPrayerAudioError(
                                    error.message ?: "تعذر إيقاف التسجيل"
                                )
                            )
                        }
                    }
                }

                is RegisterEvent.PlayPrayerRecording -> {
                    scope.launch {
                        runCatching {
                            if (event.playbackToStop != null) {
                                recordingManager.stopPlayback()
                            }
                            event.recordingToStop?.let { stoppedPrayType ->
                                val recorded = recordingManager.stopRecording()
                                if (recorded.filePath.isNotBlank()) {
                                    viewModel.handleIntent(
                                        RegisterIntent.OnRecordingCaptured(
                                            prayType = stoppedPrayType,
                                            audioPath = recorded.filePath,
                                            durationMillis = recorded.durationMillis,
                                        )
                                    )
                                }
                            }
                            recordingManager.play(event.audioPath)
                        }.onFailure { error ->
                            viewModel.handleIntent(
                                RegisterIntent.OnPrayerAudioError(
                                    error.message ?: "تعذر تشغيل التسجيل"
                                )
                            )
                        }
                    }
                }


                is RegisterEvent.NavigateToOtp -> {
                    navigator.navigateToOuter(
                        route = Screens.Otp(event.email),
                        popUpTo = Screens.Register,
                        inclusive = true,
                    )
                }

                is RegisterEvent.ShowError ->{
                    print(event.message)
                    snackbarHostState. showSnackbar(event.message,)
                }
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
                    RegisterStep.PRAYER_VOICES -> PrayerVoicesStep(state, recordingManager,viewModel::handleIntent)
                    RegisterStep.PROFILE_PICTURE -> ProfilePictureStep(state, viewModel::handleIntent)

                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) { data ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF212121),
                    shadowElevation = 8.dp,
                    border = BorderStroke(1.dp, Color(0xFF333333))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = data.visuals.message,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )

                        data.visuals.actionLabel?.let { actionText ->
                            TextButton(onClick = { data.performAction() }) {
                                Text(text = actionText, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}








