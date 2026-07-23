package org.muslim_voice.project.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.muslim_voice.project.core.data.network.createHttpClient
import org.muslim_voice.project.core.data.remote.webservice.PrayerTimesWebService
import org.muslim_voice.project.core.data.repository.PrayerTimesRepositoryImpl
import org.muslim_voice.project.core.domain.repository.PrayerTimesRepository
import org.muslim_voice.project.core.domain.usecase.GetPrayerTimesUseCase

val dataModule = module {
    single<HttpClient> { createHttpClient() }
    single { PrayerTimesWebService(get()) }
    single<PrayerTimesRepository> { PrayerTimesRepositoryImpl(get()) }
    single { GetPrayerTimesUseCase(get()) }
}
