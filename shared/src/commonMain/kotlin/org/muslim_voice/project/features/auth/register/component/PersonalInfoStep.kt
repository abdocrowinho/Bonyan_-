package org.muslim_voice.project.features.auth.register.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.component.AppDatePickerField
import org.muslim_voice.project.core.component.AppDropdownField
import org.muslim_voice.project.core.component.AppTextField
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState

@Composable
 fun PersonalInfoStep(
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
            value = state.registerInfo.firstName,
            onValueChange = { onIntent(RegisterIntent.OnFirstNameChanged(it)) },
            label = "الاسم الأول",
            errorMessage = state.step1Errors["firstName"],
        )
        AppTextField(
            value = state.registerInfo.lastName,
            onValueChange = { onIntent(RegisterIntent.OnLastNameChanged(it)) },
            label = "اسم العائلة",
            errorMessage = state.step1Errors["lastName"],
        )
        AppTextField(
            value = state.registerInfo.email,
            onValueChange = { onIntent(RegisterIntent.OnEmailChanged(it)) },
            label = "Email",
            errorMessage = state.step1Errors["email"],
            keyboardType = KeyboardType.Email,
        )
        AppTextField(
            value = state.registerInfo.password,
            onValueChange = { onIntent(RegisterIntent.OnPasswordChanged(it)) },
            label = "Password",
            errorMessage = state.step1Errors["password"],
            isPassword = true,
        )
        AppDatePickerField(
            selectedDate = state.registerInfo.birthDate,
            onDateSelected = { onIntent(RegisterIntent.OnBirthDateSelected(it)) },
            label = "تاريخ الميلاد",
            errorMessage = state.step1Errors["birthDate"],
        )
        AppTextField(
            value = state.registerInfo.roleModel,
            onValueChange = { onIntent(RegisterIntent.OnRoleModelChanged(it)) },
            label = "القدوة",
            errorMessage = state.step1Errors["roleModel"],
        )
        AppTextField(
            value = state.registerInfo.favoriteSurah,
            onValueChange = { onIntent(RegisterIntent.OnFavoriteSurahChanged(it)) },
            label = "السورة المفضلة",
            errorMessage = state.step1Errors["favoriteSurah"],
        )
        AppTextField(
            value = state.registerInfo.favoriteAyah,
            onValueChange = { onIntent(RegisterIntent.OnFavoriteAyahChanged(it)) },
            label = "الآية المفضلة",
            errorMessage = state.step1Errors["favoriteAyah"],
        )
        AppDropdownField(
            selected = state.registerInfo.selectedCountry,
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
