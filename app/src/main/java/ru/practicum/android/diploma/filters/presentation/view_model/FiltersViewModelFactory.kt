package ru.practicum.android.diploma.filters.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor

class FiltersViewModelFactory(
    private val chooseCountryInteractor: ChooseCountryInteractor,
    private val chooseRegionInteractor: ChooseRegionInteractor,
    private val chooseIndustryInteractor: ChooseIndustryInteractor,
    private val filterInteractor: FilterInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FiltersViewModel(
            chooseCountryInteractor = chooseCountryInteractor,
            chooseRegionInteractor = chooseRegionInteractor,
            chooseIndustryInteractor = chooseIndustryInteractor,
            filterInteractor = filterInteractor,
        ) as T
    }
}