package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo


interface FavoriteInteractor {
    suspend fun getFavorites(): Flow<List<Vacancy>>
    suspend fun removeVacancy(id: String): Flow<Int>
    suspend fun addVacancyToFavorites(vacancy: VacancyDetailnfo): Flow<Unit>
    suspend fun checkIfVacancyInFavorite(id: String): Boolean
    suspend fun getDetailVacancyById(id: String): Flow<DetailVacancyResult>
    suspend fun removeVacancyFromFavorites(id: String): Flow<Int>
    suspend fun getDetailVacancyByIdFromBD(id: String): Flow<VacancyDetailnfo>
}