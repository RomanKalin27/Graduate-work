package ru.practicum.android.diploma.vacancy.domain.models

sealed class DetailVacancyResult {
    object NoInternet : DetailVacancyResult()
    object EmptyResult : DetailVacancyResult()
    object AddedToFavorite : DetailVacancyResult()
    object NoFavorite : DetailVacancyResult()
    data class Success(val response: VacancyDetailnfo) : DetailVacancyResult()
    data class Error(val exception: Exception) : DetailVacancyResult()
}
