package ru.practicum.android.diploma.filters.domain.impl

import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.models.AreaDomain
import ru.practicum.android.diploma.filters.domain.models.Areas

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
    private val converter: FilterModelConverter
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

    override fun filterConvertCountry(areas: List<Areas>, country: Areas): List<AreaDomain> {
        val result = converter.regionDTOListToAreaList(areas.flatMap { it.areas }
            .filter { it.parentId == country.id })
        return result
    }

    override fun filterConvertRegions(areas: List<Areas>): List<AreaDomain> {
        val result = converter.regionDTOListToAreaList(areas.flatMap { it.areas })
        return result
    }


}