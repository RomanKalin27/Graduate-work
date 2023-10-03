package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchResult

class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {
    override suspend fun searchVacancies(queryParams: Map<String, String>): Flow<SearchResult> =
        flow {
            try {
                val response = apiService.searchVacancies(queryParams)
                emit(SearchResult.Success(response))
            } catch (e: Exception) {
                emit(SearchResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}

