package ru.practicum.android.diploma.filters.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult

interface ChooseRegionRepository {
    suspend fun getRegions(): Flow<ChooseRegionsResult>
}