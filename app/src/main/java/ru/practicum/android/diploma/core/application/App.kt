package ru.practicum.android.diploma.core.application

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

      //  startKoin {
          //  androidLogger(Level.DEBUG)
         //   androidContext(this@App)
         //   modules(dataModule, domainModule, repositoryModule, viewModelModule)
        }

    }