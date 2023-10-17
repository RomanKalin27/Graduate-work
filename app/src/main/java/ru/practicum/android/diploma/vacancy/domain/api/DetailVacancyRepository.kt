package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

interface DetailVacancyRepository {
    suspend fun addVacancyToFavorites(vacancy: VacancyDetailnfo): Flow<Unit>
    suspend  fun checkIfVacancyInFavorites(id: String): Boolean
    suspend fun getDetailVacancyById(id: String): Flow<DetailVacancyResult>
    suspend fun removeVacancyFromFavorites(id: String): Flow<Int>
}