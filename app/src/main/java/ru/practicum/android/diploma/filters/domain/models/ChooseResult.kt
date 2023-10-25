package ru.practicum.android.diploma.filters.domain.models


sealed class ChooseResult {
    data class Success(val response: List<Areas>) : ChooseResult()
    data class Error(val exception: Exception) : ChooseResult()
    object NoInternet : ChooseResult()
    object EmptyResult : ChooseResult()
}