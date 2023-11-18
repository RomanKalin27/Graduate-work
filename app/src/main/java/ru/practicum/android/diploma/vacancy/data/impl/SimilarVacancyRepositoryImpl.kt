package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.vacancy.domain.api.SimilarVacancyRepository
import javax.inject.Inject

class SimilarVacancyRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val converter: ModelConverter,
) : SimilarVacancyRepository {
    override suspend fun searchSimilarVacancies(id: String): Flow<SearchVacancyResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(SearchVacancyResult.NoInternet)
                    return@flow
                }
                val response = apiService.getSimilarVacanciesById(id)

                if (response.items.isEmpty()) {
                    emit(SearchVacancyResult.EmptyResult)
                } else {
                    val convertedResponse = converter.convertVacanciesResponse(response)
                    emit(SearchVacancyResult.Success(convertedResponse))
                }
            } catch (e: Exception) {
                emit(SearchVacancyResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}