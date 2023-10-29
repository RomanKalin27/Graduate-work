package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow

import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.api.DetailVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

class FavoriteInteractorImpl(
    private val favoriterepository: FavoriteVacancyRepository,
    private val detailVacancyRepository: DetailVacancyRepository
) :
    FavoriteInteractor {
    override suspend fun getFavorites(): Flow<List<Vacancy>> {
        return favoriterepository.getFavoriteVacancies()
    }

    override suspend fun removeVacancy(id: String): Flow<Int> {
        return favoriterepository.removeVacancy(id)
    }
    override suspend fun addVacancyToFavorites(vacancy: VacancyDetailnfo): Flow<Unit> {
        return detailVacancyRepository.addVacancyToFavorites(vacancy)
    }

    override suspend fun checkIfVacancyInFavorite(id: String): Boolean {
        return detailVacancyRepository.checkIfVacancyInFavorites(id)
    }

    override suspend fun getDetailVacancyById(id: String): Flow<DetailVacancyResult> {
        return detailVacancyRepository.getDetailVacancyById(id)
    }

    override suspend fun removeVacancyFromFavorites(id: String): Flow<Int> {
        return detailVacancyRepository.removeVacancyFromFavorites(id)
    }

    override suspend fun getDetailVacancyByIdFromBD(id: String): Flow<VacancyDetailnfo> {
        return detailVacancyRepository.getDetailVacancyByIdFromBD(id)
    }
}