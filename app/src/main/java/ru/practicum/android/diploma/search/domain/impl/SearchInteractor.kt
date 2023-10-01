package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.domain.api.SearchRepository

class SearchInteractor(private val searchRepository: SearchRepository) {

    suspend fun execute(queryParams: Map<String, String>): Flow<VacanciesResponse> {
        return searchRepository.searchVacancies(queryParams)
    }
}