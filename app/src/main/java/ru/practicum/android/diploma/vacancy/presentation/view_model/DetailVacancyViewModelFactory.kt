package ru.practicum.android.diploma.vacancy.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor

class DetailVacancyViewModelFactory(
    private val favoriteVacancyInteractor: FavoriteInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailVacancyViewModel(
            favoriteVacancyInteractor = favoriteVacancyInteractor
        ) as T
    }
}