package fr.ekito.myweatherapp.di

import fr.ekito.myweatherapp.data.repository.WeatherRepository
import fr.ekito.myweatherapp.data.repository.WeatherRepositoryImpl
import fr.ekito.myweatherapp.util.rx.ApplicationSchedulerProvider
import fr.ekito.myweatherapp.util.rx.SchedulerProvider
import fr.ekito.myweatherapp.view.detail.DetailViewModel
import fr.ekito.myweatherapp.view.splash.SplashViewModel
import fr.ekito.myweatherapp.view.weather.WeatherViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * App Components
 */
val weatherAppModule = module {

    viewModel { DetailViewModel(get(), get()) }

    // ViewModel for Search View
    viewModel { SplashViewModel(get(), get()) }

    // WeatherViewModel declaration for Weather View components
    viewModel { WeatherViewModel(get(), get()) }

    // Weather Data Repository
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }

    // Rx Schedulers
    single<SchedulerProvider> { ApplicationSchedulerProvider() }
}

// Gather all app modules
val onlineWeatherApp = listOf(weatherAppModule, remoteDatasourceModule)
val offlineWeatherApp = listOf(weatherAppModule, localAndroidDatasourceModule)