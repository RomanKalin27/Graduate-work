package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.search.data.dto.VacanciesResponse

sealed class SearchResult {
    data class Success(val response: VacanciesResponse) : SearchResult()
    data class Error(val exception: Exception) : SearchResult()
}