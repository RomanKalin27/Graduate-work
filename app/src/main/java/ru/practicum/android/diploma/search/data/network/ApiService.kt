package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.data.dto.response_models.Area

interface ApiService {

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: HH_vacancy_search (g-926@ya.ru)"
    )
    @GET("/vacancies")
    suspend fun searchVacancies(
        @QueryMap queryParams: Map<String, String>
    ): VacanciesResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: HH_vacancy_search (g-926@ya.ru)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(
        @Path("vacancy_id") id: String
    ): VacancyDetailsResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: HH_vacancy_search (g-926@ya.ru)"
    )
    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacanciesById(
        @Path("vacancy_id") id: String,
    ): VacanciesResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: HH_vacancy_search (g-926@ya.ru)"
    )
    @GET("/areas")
    suspend fun getAllCountries(): <List<Area>>
/*Либо
@GET("/areas")
suspend fun getAreas(
): List<AreasDto>
*/

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: HH_vacancy_search (g-926@ya.ru)"
    )
    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDto>
}