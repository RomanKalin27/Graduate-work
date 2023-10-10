package ru.practicum.android.diploma.filters.domain.models

import ru.practicum.android.diploma.filters.data.dto.models.ProfessionDTO

sealed class ChooseIndustryResult {
    data class Success(val response: List<ProfessionDTO>) : ChooseIndustryResult()
    data class Error(val exception: Exception) : ChooseIndustryResult()
    object NoInternet : ChooseIndustryResult()
    object EmptyResult : ChooseIndustryResult()
}