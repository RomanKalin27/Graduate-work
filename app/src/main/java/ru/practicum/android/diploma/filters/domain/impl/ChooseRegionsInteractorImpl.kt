package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult

class ChooseRegionsInteractorImpl(private val repository: ChooseRegionRepository) :
    ChooseRegionInteractor {
    override suspend fun getAreas(): Flow<ChooseRegionsResult> {
        return repository.getRegions()
    }
}