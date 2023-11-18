package ru.practicum.android.diploma.vacancy.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.practicum.android.diploma.vacancy.domain.impl.SimilarVacancyInteractor

class SimilarVacancyViewModelFactory(
    private val similarVacancyInteractor: SimilarVacancyInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SimilarVacanciesViewModel(
            similarVacancyInteractor = similarVacancyInteractor
        ) as T
    }
}