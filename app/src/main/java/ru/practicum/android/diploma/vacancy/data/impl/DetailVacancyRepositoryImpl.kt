package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.db.domain.api.VacancyDbRepository
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.vacancy.domain.api.DetailVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

class DetailVacancyRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val converter: ModelConverter,
    private val vacancyDb: VacancyDbRepository,
) : DetailVacancyRepository {

    override suspend fun getDetailVacancyById(id: String): Flow<DetailVacancyResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(DetailVacancyResult.NoInternet)
                    return@flow
                }
                val response = apiService.getVacancyById(id)
                if (response.id.isEmpty()) {
                    emit(DetailVacancyResult.EmptyResult)
                } else {
                    val convertedResponse = converter.mapDetails(response)
                    emit(DetailVacancyResult.Success(convertedResponse))
                }
            } catch (e: Exception) {
                emit(DetailVacancyResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun addVacancyToFavorites(vacancy: VacancyDetailnfo): Flow<Unit> {
        return vacancyDb.addVacancyToFavorite(vacancy)
    }

    override suspend fun checkIfVacancyInFavorites(id: String): Boolean {
        return vacancyDb.isVacancyInFavs(id)
    }

    override suspend fun removeVacancyFromFavorites(id: String): Flow<Int> {
        return vacancyDb.removeVacancyFromFavorite(id)
    }

}