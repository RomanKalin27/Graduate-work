package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.db.data.impl.VacancyDbRepositoryImpl
import ru.practicum.android.diploma.db.domain.api.VacancyDbRepository
import ru.practicum.android.diploma.filters.data.repository.ChooseCountryRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.ChooseIndustryRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.ChooseRegionRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.vacancy.data.impl.DetailVacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.DetailVacancyRepository

val repositoryModule = module {

    factory<SearchRepository> {
        SearchRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get(),
            sharedPreferences = get(),
        )
    }

    single<FilterRepository> {
        FilterRepositoryImpl(
            sharedPrefs = get()
        )
    }

    single<ChooseCountryRepository> {
        ChooseCountryRepositoryImpl(
            apiService = get(),
            networkControl = get()
        )
    }

    single<ChooseRegionRepository> {
        ChooseRegionRepositoryImpl(
            apiService = get(),
            networkControl = get(),
        )
    }
    single<ChooseIndustryRepository> {
        ChooseIndustryRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            filterModelConverter = get()
        )
    }

    single<VacancyDbRepository> {
        VacancyDbRepositoryImpl(
            appDataBase = get(),
            vacancyDbConverter = get()
        )
    }

    single<DetailVacancyRepository> {
        DetailVacancyRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get(),
            vacancyDb = get()
        )
    }
}

