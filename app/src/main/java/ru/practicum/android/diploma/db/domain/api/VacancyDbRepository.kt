package ru.practicum.android.diploma.db.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.db.data.entity.VacancyEntity
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

interface VacancyDbRepository {
    suspend fun insertFavouriteVacancy(vacancyEntity: VacancyEntity)
    suspend fun getFavouriteVacancies(): Flow<List<Vacancy>>
    suspend fun getFavouriteVacancyDetailsById(vacancyId: String): Flow<VacancyDetails?>
    suspend fun getFavouriteVacancyById(vacancyId: String): Flow<Vacancy?>
    suspend fun deleteFavouriteVacancyById(vacancyId: String)
}