package ru.practicum.android.diploma.db.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.db.domain.AppDB
import ru.practicum.android.diploma.db.domain.api.VacancyDbRepository
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

class VacancyDbRepositoryImpl(
    private val appDataBase: AppDB,
    private val vacancyDbConverter: ModelConverter,
) : VacancyDbRepository {

    override suspend fun removeVacancyFromFavorite(id: String): Flow<Int> {
        return flowOf(appDataBase.vacancyDao().delete(id))
    }

    override suspend fun addVacancyToFavorite(vacancy: VacancyDetailnfo): Flow<Unit> {
        return flowOf(
            appDataBase.vacancyDao()
                .insertFavouriteVacancy(vacancyDbConverter.toFullInfoEntity(vacancy))
        )
    }

//    override suspend fun showIfInFavouriteById(id: String): Flow<Boolean> {
//        return appDataBase.vacancyDao().showIfInFavouriteById(id).map { it }
//    }

    override suspend fun isVacancyInFavs(id: String): Boolean {
        return appDataBase.vacancyDao().isVacancyInFavs(id)
    }

    override suspend fun getFavoritesById(id: String): Flow<VacancyDetailnfo> {
        return appDataBase.vacancyDao().getFavoritesById(id)
            .map { vacancyDbConverter.entityToModel(it) }
    }

    override suspend fun getFavsVacancies(): Flow<List<Vacancy>> {
        return appDataBase.vacancyDao().getFavorites().map { vacancyDbConverter.mapToVacancies(it) }
    }
}