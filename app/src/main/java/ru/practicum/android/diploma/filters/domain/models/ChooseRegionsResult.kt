package ru.practicum.android.diploma.filters.domain.models

import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO

sealed class ChooseRegionsResult {
    data class Success(val response: List<AreasDTO>) : ChooseRegionsResult()
    data class Error(val exception: Exception) : ChooseRegionsResult()
    object NoInternet : ChooseRegionsResult()
    object EmptyResult : ChooseRegionsResult()
}