package ru.practicum.android.diploma.search.domain.models


sealed class SearchVacancyResult {
    object NoInternet : SearchVacancyResult()
    object EmptyResult : SearchVacancyResult()
    object Loading : SearchVacancyResult()
    data class Success(val response: ConvertedResponse) : SearchVacancyResult()
    data class Error(val exception: Exception) : SearchVacancyResult()
    object StartScreen : SearchVacancyResult()
}