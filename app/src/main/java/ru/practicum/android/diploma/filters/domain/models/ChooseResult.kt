package ru.practicum.android.diploma.filters.domain.models

import ru.practicum.android.diploma.filters.data.dto.models.CountryDTO

sealed class ChooseResult {
    data class Success(val response: List<CountryDTO>) : ChooseResult()
    data class Error(val exception: Exception) : ChooseResult()
    object NoInternet : ChooseResult()
    object EmptyResult : ChooseResult()
}