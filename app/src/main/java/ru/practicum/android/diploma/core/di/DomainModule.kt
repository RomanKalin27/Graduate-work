package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor

val domainModule = module {
    single { SearchInteractor(get()) }
}