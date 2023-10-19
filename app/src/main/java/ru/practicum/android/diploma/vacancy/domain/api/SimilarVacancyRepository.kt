package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

interface SimilarVacancyRepository {
    suspend fun searchSimilarVacancies(id: String): Flow<SearchVacancyResult>
}