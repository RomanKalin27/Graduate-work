package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.search.data.network.ApiService


val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}