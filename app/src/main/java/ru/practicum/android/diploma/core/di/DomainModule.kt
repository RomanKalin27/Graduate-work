package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.impl.ChooseCountryInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.ChooseIndustryInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.ChooseRegionsInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.DetailVacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.SimilarVacancyInteractor

val domainModule = module {
    single {
        SearchInteractor(
            searchRepository = get()
        )
    }

    single<FilterInteractor> {
        FilterInteractorImpl(
            filterRepository = get()
        )
    }
    single<ChooseCountryInteractor> {
        ChooseCountryInteractorImpl(
            repository = get()
        )
    }
    single<ChooseRegionInteractor> {
        ChooseRegionsInteractorImpl(
            repository = get()
        )
    }
    single<ChooseIndustryInteractor> {
        ChooseIndustryInteractorImpl(
            repository = get()
        )
    }
    single {
        DetailVacancyInteractor(
            detailVacancyRepository = get()
        )
    }
    single {
        SimilarVacancyInteractor(
            similarVacancyRepository = get()
        )
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(
            repository = get()
        )
    }
}