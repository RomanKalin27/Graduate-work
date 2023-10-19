package ru.practicum.android.diploma.filters.domain.api

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
}