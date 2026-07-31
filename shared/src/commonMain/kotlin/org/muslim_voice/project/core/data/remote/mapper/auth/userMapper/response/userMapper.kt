package org.muslim_voice.project.core.data.remote.mapper.auth.userMapper.response

import org.muslim_voice.project.core.data.remote.dto.user.response.UserDto
import org.muslim_voice.project.core.data.remote.mapper.auth.registerMapper.request.toDomain
import org.muslim_voice.project.core.domain.model.auth.user.UserModel

fun UserDto.toDomain(): UserModel {
    return UserModel(
        id = id ?: -1,
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email.orEmpty(),
        isVerified = isVerified ?: false,
        profilePicture = profilePicture,
        country = country,
        birthdate = birthdate,
        roleModel = roleModel,
        favSurah = favSurah,
        favAyah = favAyah,
        createdAt = createdAt,
        prayerRecordings = prayerRecordings?.toDomain()
    )
}
 fun UserModel.empty() = UserModel(
    id = -1,
    firstName = "",
    lastName = "",
    email = "",
    isVerified = false,
    profilePicture = null,
    country = null,
    birthdate = null,
    roleModel = null,
    favSurah = null,
    favAyah = null,
    createdAt = null,
    prayerRecordings = null
)