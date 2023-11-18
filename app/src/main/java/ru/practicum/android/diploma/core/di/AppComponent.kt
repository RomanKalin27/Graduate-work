package ru.practicum.android.diploma.core.di

import dagger.Component
import ru.practicum.android.diploma.favorites.presentation.ui.FavoriteFragment
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment
import ru.practicum.android.diploma.filters.presentation.ui.ChooseIndustry
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment
import ru.practicum.android.diploma.filters.presentation.ui.FiltersFragment
import ru.practicum.android.diploma.search.presentation.ui.SearchFragment
import ru.practicum.android.diploma.vacancy.presentation.ui.SimilarVacancyFragment
import ru.practicum.android.diploma.vacancy.presentation.ui.VacancyFragment

@Component(modules = [ViewModelModule::class, DomainModule::class, RepositoryModule::class, DataModule::class])
interface AppComponent {
fun injectSearchFragment(searchFragment: SearchFragment)
    fun injectFiltersFragment(filtersFragment: FiltersFragment)
    fun injectChooseCountryFragment(chooseCountryFragment: ChooseCountryFragment)
    fun injectChooseRegionFragment(chooseRegionFragment: ChooseRegionFragment)
    fun injectChoosePlaceWorkFragment(choosePlaceWorkFragment: ChoosePlaceWorkFragment)
    fun injectChooseIndustryFragment(chooseIndustryFragment: ChooseIndustry)
    fun injectFavoriteFragment(favoriteFragment: FavoriteFragment)
    fun injectVacancyFragment(vacancyFragment: VacancyFragment)
    fun injectSimilarVacancyFragment(similarVacancyFragment: SimilarVacancyFragment)
}