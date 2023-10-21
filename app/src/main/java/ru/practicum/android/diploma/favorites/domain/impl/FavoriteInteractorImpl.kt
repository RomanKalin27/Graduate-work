package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow

import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoriteInteractorImpl(private val repository: FavoriteVacancyRepository) :
    FavoriteInteractor {
    override suspend fun getFavorites(): Flow<List<Vacancy>> {
        return repository.getFavoriteVacancies()
    }

    override suspend fun removeVacancy(id: String): Flow<Int> {
        return repository.removeVacancy(id)
    }
}