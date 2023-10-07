package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseResult

class ChooseCountryInteractorImpl(private val repository: ChooseCountryRepository) :
    ChooseCountryInteractor {
    override suspend fun execute(): Flow<ChooseResult> {
        return repository.getCountry()
    }
}