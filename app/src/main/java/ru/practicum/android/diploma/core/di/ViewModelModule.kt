package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.view_model.FavoriteViewModel
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.view_model.DetailVacancyViewModel
import ru.practicum.android.diploma.vacancy.presentation.view_model.SimilarVacanciesViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    viewModel {
        FiltersViewModel(
            filterInteractor = get(),
            chooseCountryInteractor = get(),
            chooseRegionInteractor = get(),
            chooseIndustryInteractor = get(),
            get()
        )
    }
    viewModel {
        FavoriteViewModel(
            favoriteVacancyInteractor = get()
        )
    }
    viewModel {
        DetailVacancyViewModel(
            detailVacancyInteractor = get()
        )
    }
    viewModel {
        SimilarVacanciesViewModel(
            similarVacancyInteractor = get()
        )
    }
}
