package ru.practicum.android.diploma.vacancy.domain.models

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.db.domain.models.Vacancy

interface VacancyInteractor {
    suspend fun loadVacancyDetails(vacancyId: String): Pair<VacancyDetails?, String?>
    fun getSimilarVacanciesById (vacancyId: String): Flow<Pair<List<Vacancy>?, String?>>
    fun shareVacancyUrl(vacancyUrl: String)
    fun sharePhone(phone: String)
    fun shareEmail(email: String)
}