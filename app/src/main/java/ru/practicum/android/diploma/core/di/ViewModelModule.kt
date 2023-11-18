package ru.practicum.android.diploma.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.presentation.view_model.FavoriteViewModelFactory
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModelFactory
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModelFactory
import ru.practicum.android.diploma.vacancy.domain.impl.SimilarVacancyInteractor
import ru.practicum.android.diploma.vacancy.presentation.view_model.DetailVacancyViewModelFactory
import ru.practicum.android.diploma.vacancy.presentation.view_model.SimilarVacancyViewModelFactory

@Module
class ViewModelModule(val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }
    @Provides
    fun provideSearchViewModel(searchInteractor: SearchInteractor): SearchViewModelFactory {
        return SearchViewModelFactory(
            searchInteractor = searchInteractor
        )
    }
    @Provides
    fun provideFiltersViewModel(
        chooseCountryInteractor: ChooseCountryInteractor,
        chooseRegionInteractor: ChooseRegionInteractor,
        chooseIndustryInteractor: ChooseIndustryInteractor,
        filterInteractor: FilterInteractor,
    ): FiltersViewModelFactory {
        return FiltersViewModelFactory(
            chooseCountryInteractor = chooseCountryInteractor,
            chooseRegionInteractor = chooseRegionInteractor,
            chooseIndustryInteractor = chooseIndustryInteractor,
            filterInteractor = filterInteractor,
        )
    }
    @Provides
    fun provideFavoriteViewModel(favoriteVacancyInteractor: FavoriteInteractor): FavoriteViewModelFactory {
        return FavoriteViewModelFactory(
            favoriteVacancyInteractor = favoriteVacancyInteractor
        )
    }
    @Provides
    fun provideDetailVacancyViewModel(favoriteVacancyInteractor: FavoriteInteractor): DetailVacancyViewModelFactory {
        return DetailVacancyViewModelFactory(
            favoriteVacancyInteractor = favoriteVacancyInteractor
        )
    }
    @Provides
    fun provideSimilarVacancyViewModel(similarVacancyInteractor: SimilarVacancyInteractor): SimilarVacancyViewModelFactory {
        return SimilarVacancyViewModelFactory(
            similarVacancyInteractor = similarVacancyInteractor
        )
    }
}
/*val viewModelModule = module {

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
        )
    }
    viewModel {
        FavoriteViewModel(
            favoriteVacancyInteractor = get()
        )
    }
    viewModel {
        DetailVacancyViewModel(
            favoriteVacancyInteractor = get()
        )
    }
    viewModel {
        SimilarVacanciesViewModel(
            similarVacancyInteractor = get()
        )
    }
}*/
