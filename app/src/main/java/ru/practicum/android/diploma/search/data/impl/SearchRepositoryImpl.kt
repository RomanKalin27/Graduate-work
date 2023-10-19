package ru.practicum.android.diploma.search.data.impl

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.AREA_ID_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.COUNTRY_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.EXPECTED_SALARY_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.FILTERS_ON_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.INDUSTRY_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.NO_SALARY_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.REGION_KEY
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.search.data.dto.response_models.Area
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val converter: ModelConverter,
    private val sharedPreferences: SharedPreferences,
) : SearchRepository {
    override suspend fun searchVacancies(query: String): Flow<SearchVacancyResult> =
        flow {
            //val queryParams = (mapOf("text" to "${query}", "per_page" to "50"))
            val queryParams = requestMaker(query)
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(SearchVacancyResult.NoInternet)
                    return@flow
                }
                val response = apiService.searchVacancies(queryParams)

                if (response.items.isEmpty()) {
                    4
                    emit(SearchVacancyResult.EmptyResult)
                } else {
                    val convertedResponse = converter.convertVacanciesResponse(response)
                    emit(SearchVacancyResult.Success(convertedResponse))
                }
            } catch (e: Exception) {
                emit(SearchVacancyResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)


    private fun requestMaker(query: String): Map<String, String> {
        var params = (mapOf("text" to "${query}", "per_page" to "50"))
        val country = sharedPreferences.getString(COUNTRY_KEY, null)
        val region = sharedPreferences.getString(REGION_KEY, null)
        if (!country.isNullOrEmpty()) {
            if (!region.isNullOrEmpty()) {
                val regionId = Json.decodeFromString<Area>(region).id
                params += Pair("area", regionId ?: "")
            } else {
                val countryId = Json.decodeFromString<Area>(country).id
                params += Pair("area", countryId ?: "")
            }
        }
        val industry = sharedPreferences.getString(INDUSTRY_KEY, null)
        if (!industry.isNullOrEmpty()) {
            val industryId = Json.decodeFromString<Industry>(industry).id
            params += Pair("industry", industryId)
        }
        val salary = sharedPreferences.getString(EXPECTED_SALARY_KEY, null)
        if (!salary.isNullOrEmpty()) {
            params += Pair("salary", salary)
        }
        val noSalary = sharedPreferences.getBoolean(NO_SALARY_KEY, false)
        params += Pair("only_with_salary", "$noSalary")
        return params
    }

    override fun isFiltersOn(): Boolean {
        return sharedPreferences.getBoolean(FILTERS_ON_KEY, false)
    }
}

