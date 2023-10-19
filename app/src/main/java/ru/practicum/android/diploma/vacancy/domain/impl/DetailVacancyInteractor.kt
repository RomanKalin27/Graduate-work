package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.api.DetailVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

class DetailVacancyInteractor(private val detailVacancyRepository: DetailVacancyRepository) {
    suspend fun addVacancyToFavorites(vacancy: VacancyDetailnfo): Flow<Unit> {
        return detailVacancyRepository.addVacancyToFavorites(vacancy)
    }

    suspend fun checkIfVacancyInFavorite(id: String): Boolean {
        return detailVacancyRepository.checkIfVacancyInFavorites(id)
    }

    suspend fun getDetailVacancyById(id: String): Flow<DetailVacancyResult> {
        return detailVacancyRepository.getDetailVacancyById(id)
    }

    suspend fun removeVacancyFromFavorites(id: String): Flow<Int> {
        return detailVacancyRepository.removeVacancyFromFavorites(id)
    }

}