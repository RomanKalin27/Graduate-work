package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository

val repositoryModule = module {

    factory<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    single<FilterRepository> {FilterRepositoryImpl(get())}

}