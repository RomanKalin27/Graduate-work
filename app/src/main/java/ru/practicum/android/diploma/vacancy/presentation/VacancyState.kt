package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

sealed class VacancyState{
    object Loading: VacancyState()
    class Content(val vacancyDetails: VacancyDetails): VacancyState()
    class Error( val errorMessage: String, ): VacancyState()
}