package ru.practicum.android.diploma.filters.domain.impl

import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
) : FilterInteractor {
    override fun saveFilters(
        countryJson: String?,
        regionJson: String?,
        industryJson: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
    ) {
        filterRepository.saveFilters(
            countryJson,
            regionJson,
            industryJson,
            expectedSalary,
            removeNoSalary,
        )
    }

    override fun getCountry(): String? {
        return filterRepository.getCountry()
    }

    override fun getRegion(): String? {
        return filterRepository.getRegion()
    }

    override fun getIndustry(): String? {
        return filterRepository.getIndustry()
    }

    override fun getExpectedSalary(): String? {
        return filterRepository.getExpectedSalary()
    }

    override fun getRemoveNoSalary(): Boolean {
        return filterRepository.getRemoveNoSalary()
    }

    override fun removeFilters() {
        filterRepository.removeFilters()
    }
}