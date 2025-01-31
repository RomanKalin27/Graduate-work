package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO
import ru.practicum.android.diploma.filters.data.dto.models.CategoriesDTO
import ru.practicum.android.diploma.filters.data.dto.models.CountryDTO
import ru.practicum.android.diploma.filters.data.dto.models.IndustryDTO
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.vacancy.data.network.dto.VacancyDetailModelDTO

interface ApiService {

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: HH_vacancy_search (g-926@ya.ru)"
    )
    @GET("/vacancies")
    suspend fun searchVacancies(
        @QueryMap queryParams: Map<String, String>,
    ): VacanciesResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(
        @Path("vacancy_id") id: String,
    ): VacancyDetailModelDTO

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacanciesById(
        @Path("vacancy_id") id: String,
    ): VacanciesResponse

    @GET("/areas")
    suspend fun getAreas(): List<AreasDTO>

    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDTO>

    @GET("/areas/countries")
    suspend fun getCountry(): List<CountryDTO>

    @GET("/professional_roles")
    suspend fun getSpecializations(): CategoriesDTO
}