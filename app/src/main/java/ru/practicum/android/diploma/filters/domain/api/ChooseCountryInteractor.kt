package ru.practicum.android.diploma.filters.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.models.ChooseResult

interface ChooseCountryInteractor {
    suspend fun execute(): Flow<ChooseResult>
}