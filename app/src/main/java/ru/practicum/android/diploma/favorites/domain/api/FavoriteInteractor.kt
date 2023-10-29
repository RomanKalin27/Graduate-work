package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy


interface FavoriteInteractor {
    suspend fun getFavorites(): Flow<List<Vacancy>>
    suspend fun removeVacancy(id: String): Flow<Int>
}