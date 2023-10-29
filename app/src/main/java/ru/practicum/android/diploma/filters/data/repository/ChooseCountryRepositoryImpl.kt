package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseResult
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter

class ChooseCountryRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val converter: ModelConverter,
) : ChooseCountryRepository {
    override suspend fun getCountry(): Flow<ChooseResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(ChooseResult.NoInternet)
                    return@flow
                }
                val response = apiService.getAreas()
                if (response.isEmpty()) {
                    emit(ChooseResult.EmptyResult)
                } else {
                    val convertedResponse = converter.convertAreasDTOListToAreasList(response)
                    emit(ChooseResult.Success(convertedResponse))
                }
            } catch (e: Exception) {
                emit(ChooseResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}