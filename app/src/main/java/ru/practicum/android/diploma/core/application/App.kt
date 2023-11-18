package ru.practicum.android.diploma.core.application
import android.app.Application
import android.content.Context
import ru.practicum.android.diploma.core.di.AppComponent
import ru.practicum.android.diploma.core.di.DaggerAppComponent
import ru.practicum.android.diploma.core.di.ViewModelModule


class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
            .viewModelModule(ViewModelModule(context = this))
            .build()
           //DaggerAppComponent.create()
        /*startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(dataModule, domainModule, repositoryModule, viewModelModule)
        }*/

    }

    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }