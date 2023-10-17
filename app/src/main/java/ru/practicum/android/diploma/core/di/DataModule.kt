package ru.practicum.android.diploma.core.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.application.App.Companion.SHARED_PREFS
import ru.practicum.android.diploma.db.data.dao.VacancyDao
import ru.practicum.android.diploma.db.domain.AppDB
import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter


val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDB::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<VacancyDao> { get<AppDB>().vacancyDao() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFS, Application.MODE_PRIVATE)
    }

    single {
        ConnectivityHelper(
            context = get()
        )
    }

    single {
        ModelConverter(
            context = get()
        )
    }

    single {
        FilterModelConverter()
    }


}