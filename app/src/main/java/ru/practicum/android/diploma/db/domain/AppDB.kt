package ru.practicum.android.diploma.db.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.db.data.dao.VacancyDao
import ru.practicum.android.diploma.db.data.entity.VacancyEntity

@Database(version = 1, entities = [VacancyEntity::class])
abstract class AppDB: RoomDatabase() {
    abstract fun vacancyDao(): VacancyDao
}