package org.muslim_voice.project.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.muslim_voice.project.core.data.local.room.AppDatabase
import org.muslim_voice.project.core.data.network.createHttpClient
import org.muslim_voice.project.core.data.remote.api.AuthApiService
import org.muslim_voice.project.core.data.remote.webservice.PrayerTimesWebService
import org.muslim_voice.project.core.data.repository.AuthStorageRepositoryImpl
import org.muslim_voice.project.core.data.repository.PrayerSoundRepository
import org.muslim_voice.project.core.data.repository.PrayerTimesRepositoryImpl
import org.muslim_voice.project.core.data.storage.SupabaseStorageManager
import org.muslim_voice.project.core.domain.repository.AuthStorageRepository
import org.muslim_voice.project.core.domain.repository.PrayerTimesRepository
import org.muslim_voice.project.core.domain.usecase.GetPrayerTimesUseCase
import org.muslim_voice.project.core.domain.usecase.LoginUseCase
import org.muslim_voice.project.core.domain.usecase.OtpUseCase
import org.muslim_voice.project.core.domain.usecase.RegisterUseCase
import org.muslim_voice.project.core.domain.validation.login.LoginValidation
import org.muslim_voice.project.core.domain.validation.otp.OtpValidation
import org.muslim_voice.project.core.domain.validation.register.RegisterValidation

val dataModule = module {
    single { get<AppDatabase>().prayerSoundDao() }
    single<HttpClient> { createHttpClient() }
    single { AuthApiService(get()) }
    single { PrayerTimesWebService(get()) }
    single { SupabaseStorageManager(get()) }
    single<PrayerTimesRepository> { PrayerTimesRepositoryImpl(get()) }
    single<AuthStorageRepository> { AuthStorageRepositoryImpl(get()) }
    single { PrayerSoundRepository(get()) }
}

val domainModule = module {
    single { GetPrayerTimesUseCase(get()) }
    single { LoginUseCase(get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single { OtpUseCase(get(), get()) }

    // validations
    single { LoginValidation() }
    single { RegisterValidation() }
    single { OtpValidation() }
}
