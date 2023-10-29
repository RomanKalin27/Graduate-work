package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter

class ChooseRegionRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val converter: ModelConverter
) : ChooseRegionRepository {
    override suspend fun getRegions(): Flow<ChooseRegionsResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(ChooseRegionsResult.NoInternet)
                    return@flow
                }
                val response = apiService.getAreas()
                if (response.isEmpty()) {
                    emit(ChooseRegionsResult.EmptyResult)
                } else {
                    val convertedResponse = converter.convertAreasDTOListToAreasList(response)
                    emit(ChooseRegionsResult.Success(convertedResponse))
                }
            } catch (e: Exception) {
                emit(ChooseRegionsResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}