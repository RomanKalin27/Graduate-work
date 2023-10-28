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
import ru.practicum.android.diploma.vacancy.domain.impl.SimilarVacancyInteractor

val domainModule = module {
    factory {
        SearchInteractor(
            searchRepository = get()
        )
    }

    factory<FilterInteractor> {
        FilterInteractorImpl(
            filterRepository = get(),
            converter = get()
        )
    }
    factory<ChooseCountryInteractor> {
        ChooseCountryInteractorImpl(
            repository = get()
        )
    }
    factory<ChooseRegionInteractor> {
        ChooseRegionsInteractorImpl(
            repository = get()
        )
    }
    factory<ChooseIndustryInteractor> {
        ChooseIndustryInteractorImpl(
            repository = get()
        )
    }

    factory {
        SimilarVacancyInteractor(
            similarVacancyRepository = get()
        )
    }

    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(
            favoriterepository = get(),
            detailVacancyRepository = get(),
        )
    }
}