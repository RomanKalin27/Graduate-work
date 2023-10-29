package ru.practicum.android.diploma.filters.domain.models

sealed class ChooseRegionsResult {
    data class Success(val response: List<Areas>) : ChooseRegionsResult()
    data class Error(val exception: Exception) : ChooseRegionsResult()
    object NoInternet : ChooseRegionsResult()
    object EmptyResult : ChooseRegionsResult()
}