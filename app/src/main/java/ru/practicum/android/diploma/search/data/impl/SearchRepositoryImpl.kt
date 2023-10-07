package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
) : SearchRepository {
    override suspend fun searchVacancies(queryParams: Map<String, String>): Flow<SearchVacancyResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(SearchVacancyResult.NoInternet)
                    return@flow
                }
                val response = apiService.searchVacancies(queryParams)

                if (response.items.isEmpty()) {
                    emit(SearchVacancyResult.EmptyResult)
                } else {
                    emit(SearchVacancyResult.Success(response))
                }
            } catch (e: Exception) {
                emit(SearchVacancyResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}

