package ru.practicum.android.diploma.db.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

interface VacancyDbRepository {
    suspend fun removeVacancyFromFavorite(id: String): Flow<Int>
    suspend fun addVacancyToFavorite(vacancy: VacancyDetailnfo): Flow<Unit>
    suspend fun isVacancyInFavs(id: String): Boolean
    suspend fun getFavoritesById(id: String): Flow<VacancyDetailnfo>
    suspend fun getFavsVacancies(): Flow<List<Vacancy>>
}