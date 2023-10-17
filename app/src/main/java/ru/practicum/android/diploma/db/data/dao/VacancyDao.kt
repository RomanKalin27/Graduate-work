package ru.practicum.android.diploma.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.db.data.entity.VacancyFullInfoEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteVacancy(vacancyEntity: VacancyFullInfoEntity)

    @Query("DELETE FROM vacancy_table WHERE id =:id")
    suspend fun delete(id: String): Int

    @Query("SELECT * FROM vacancy_table ORDER by date DESC")
    fun getFavorites(): Flow<List<VacancyFullInfoEntity>>

    @Query("SELECT * FROM vacancy_table WHERE id =:id")
    fun getFavoritesById(id: String): Flow<VacancyFullInfoEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM vacancy_table WHERE id = :id LIMIT 1);")
    suspend fun isVacancyInFavs(id: String): Boolean
}


