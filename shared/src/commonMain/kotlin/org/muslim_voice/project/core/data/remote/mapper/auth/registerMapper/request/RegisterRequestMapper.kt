package org.muslim_voice.project.core.data.remote.mapper.auth.registerMapper.request

import org.muslim_voice.project.core.domain.model.auth.register.request.RegisterRequestModel


import org.muslim_voice.project.core.data.remote.dto.auth.register.request.RecordingsDto
import org.muslim_voice.project.core.data.remote.dto.auth.register.request.RegisterRequestDto
import org.muslim_voice.project.core.domain.model.auth.register.request.RecordingsModel

fun RegisterRequestModel.toDto(): RegisterRequestDto {
    return RegisterRequestDto(
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
        favoriteAyah = favoriteAyah,
        favoriteSurah = favoriteSurah,
        country = country,
        birthDate = birthDate,
        roleModel = roleModel,
        profilePicture = profilePicture,
        recordings = recordings?.toDto()
    )
}

fun RecordingsModel.toDto(): RecordingsDto {
    return RecordingsDto(
        fajr = fajr,
        dhuhr = dhuhr,
        asr = asr,
        maghrib = maghrib,
        isha = isha
    )
}

fun RecordingsDto.toDomain(): RecordingsModel {
    return RecordingsModel(
        fajr = fajr,
        dhuhr = dhuhr,
        asr = asr,
        maghrib = maghrib,
        isha = isha
    )
}