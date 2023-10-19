package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.vacancy.domain.api.SimilarVacancyRepository

class SimilarVacancyInteractor(private val similarVacancyRepository: SimilarVacancyRepository) {
    suspend fun execute(id: String): Flow<SearchVacancyResult> {
        return similarVacancyRepository.searchSimilarVacancies(id)
    }
}