package ru.practicum.android.diploma.filters.data.repository

import android.content.SharedPreferences
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences,
) : FilterRepository {
    override fun saveFilters(
        countryJson: String?,
        regionJson: String?,
        industryJson: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
    ) {
        var isFiltersOn = false
        if (!countryJson.isNullOrEmpty() || !industryJson.isNullOrEmpty() || !expectedSalary.isNullOrEmpty() || removeNoSalary) {
            isFiltersOn = true
        }

        sharedPrefs.edit()
            .putString(COUNTRY_KEY, countryJson)
            .putString(REGION_KEY, regionJson)
            .putString(INDUSTRY_KEY, industryJson)
            .putString(EXPECTED_SALARY_KEY, expectedSalary)
            .putBoolean(NO_SALARY_KEY, removeNoSalary)
            .putBoolean(FILTERS_ON_KEY, isFiltersOn)
            .apply()
    }

    override fun getCountry(): String? {
        return sharedPrefs.getString(COUNTRY_KEY, null)
    }

    override fun getRegion(): String? {
        return sharedPrefs.getString(REGION_KEY, null)
    }

    override fun getIndustry(): String? {
        return sharedPrefs.getString(INDUSTRY_KEY, null)
    }

    override fun getExpectedSalary(): String? {
        return sharedPrefs.getString(EXPECTED_SALARY_KEY, null)
    }

    override fun getRemoveNoSalary(): Boolean {
        return sharedPrefs.getBoolean(NO_SALARY_KEY, false)
    }

    override fun removeFilters() {
        sharedPrefs.edit().clear().apply()
    }


    companion object {
        const val COUNTRY_KEY = "COUNTRY_KEY"
        const val REGION_KEY = "REGION_KEY"
        const val INDUSTRY_KEY = "INDUSTRY_KEY"
        const val EXPECTED_SALARY_KEY = "EXPECTED_SALARY_KEY"
        const val NO_SALARY_KEY = "NO_SALARY_KEY"
        const val AREA_ID_KEY = "AREA_ID_KEY"
        const val FILTERS_ON_KEY = "FILTERS_ON_KEY"
    }
}