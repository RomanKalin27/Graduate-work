package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.db.domain.AppDB
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.search.domain.models.Vacancy
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val converter: ModelConverter,
    private val appDataBase: AppDB,
) : FavoriteVacancyRepository {

    private val vacancyDb = appDataBase.vacancyDao()

    override suspend fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return vacancyDb.getFavorites().map { converter.mapToVacancies(it) }
    }

    override suspend fun removeVacancy(id: String): Flow<Int> {
        return flowOf(vacancyDb.delete(id))
    }
}