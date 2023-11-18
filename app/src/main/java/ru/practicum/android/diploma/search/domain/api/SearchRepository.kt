package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

interface SearchRepository {
    suspend fun searchVacancies(query: String, page: Int): Flow<SearchVacancyResult>
    fun isFiltersOn(): Boolean

}