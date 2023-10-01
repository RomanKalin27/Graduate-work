package ru.practicum.android.diploma.core.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.practicum.android.diploma.core.di.dataModule
import ru.practicum.android.diploma.core.di.domainModule
import ru.practicum.android.diploma.core.di.repositoryModule
import ru.practicum.android.diploma.core.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(dataModule, domainModule, repositoryModule, viewModelModule)
        }

    }
}