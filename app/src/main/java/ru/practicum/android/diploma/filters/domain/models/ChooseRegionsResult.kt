package ru.practicum.android.diploma.filters.domain.models

import ru.practicum.android.diploma.search.data.dto.response_models.Area

sealed class ChooseRegionsResult {
    data class Success(val response: List<Area>) : ChooseRegionsResult()
    data class Error(val exception: Exception) : ChooseRegionsResult()
    object NoInternet : ChooseRegionsResult()
    object EmptyResult : ChooseRegionsResult()
}