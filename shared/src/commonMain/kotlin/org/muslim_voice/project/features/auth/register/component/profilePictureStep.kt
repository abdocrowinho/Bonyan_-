package org.muslim_voice.project.features.auth.register.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.media.SharedImage
import org.muslim_voice.project.core.media.rememberImagePicker
import org.muslim_voice.project.core.permission.PermissionsManager
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState

@Composable
 fun ProfilePictureStep(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
    permissionsManager: PermissionsManager = koinInject(),
) {
    val scope = rememberCoroutineScope()
    val imagePicker = rememberImagePicker(permissionsManager)

    var selectedSharedImage by remember { mutableStateOf<SharedImage?>(null) }

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
            val imageBitmap = remember(selectedSharedImage) {
                selectedSharedImage?.toImageBitmap()
            }

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = AppColors.Primary,
                    modifier = Modifier.size(56.dp),
                )
            }
        }

        // اختيار صورة من المعرض
        AppButton(
            text = "من المعرض",
            onClick = {
                scope.launch {
                    val image = imagePicker.getImageFromGallery()
                    if (image != null) {
                        selectedSharedImage = image
                        val bytes = image.toByteArray()
                        if (bytes != null) {
                               onIntent(RegisterIntent.OnImageSelected(bytes))
                        }
                    }
                }
            },
            icon = Icons.Filled.PhotoLibrary,
            isTransparent = true,
        )

        // الالتقاط من الكاميرا
        AppButton(
            text = "من الكاميرا",
            onClick = {
                scope.launch {
                    val image = imagePicker.getImageFromCamera()
                    if (image != null) {
                        selectedSharedImage = image
                        // بنبعت الـ Bytes للـ ViewModel
                        val bytes = image.toByteArray()
                        if (bytes != null) {
                              onIntent(RegisterIntent.OnImageSelected(bytes))
                        }
                    }
                }
            },
            icon = Icons.Filled.CameraAlt,
            isTransparent = true,
        )


        // this button must make two things
        // number 1 : must make a request for register
        // number 2 : navigate to otpScreen
        AppButton(
            text = "تسجيل",
            onClick = { onIntent.invoke(RegisterIntent.SubmitProfile)  },
            enabled = state.registerInfo.profileImageBytes != null || selectedSharedImage != null,
            isLoading = state.isLoading,
        )
    }
}