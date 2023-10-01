package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.domain.api.SearchRepository

class SearchRepositoryImpl (private val apiService: ApiService) : SearchRepository {
    override suspend fun searchVacancies(queryParams: Map<String, String>): Flow<VacanciesResponse> = flow {
        val response = apiService.searchVacancies(queryParams)
        emit(response)
    }.flowOn(Dispatchers.IO)
}

