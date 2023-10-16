package ru.practicum.android.diploma.filters.domain.models

import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO

sealed class ChooseResult {
    data class Success(val response: List<AreasDTO>) : ChooseResult()
    data class Error(val exception: Exception) : ChooseResult()
    object NoInternet : ChooseResult()
    object EmptyResult : ChooseResult()
}