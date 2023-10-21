package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy


interface FavoriteVacancyRepository {
    suspend fun getFavoriteVacancies(): Flow<List<Vacancy>>
    suspend fun removeVacancy(id: String): Flow<Int>


}