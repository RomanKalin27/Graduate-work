package ru.practicum.android.diploma.filters.domain.api

import ru.practicum.android.diploma.filters.domain.models.AreaDomain
import ru.practicum.android.diploma.filters.domain.models.Areas

interface FilterInteractor {
    fun saveFilters(
        countryJson: String?,
        regionJson: String?,
        industryJson: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
    )

    fun getCountry(): String?
    fun getRegion(): String?
    fun getIndustry(): String?
    fun getExpectedSalary(): String?
    fun getRemoveNoSalary(): Boolean
    fun removeFilters()
    fun filterConvertCountry(areas: List<Areas>, country: Areas): List<AreaDomain>
    fun filterConvertRegions(areas: List<Areas>): List<AreaDomain>

}