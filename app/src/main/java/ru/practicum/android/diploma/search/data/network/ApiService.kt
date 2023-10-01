package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse

interface ApiService {

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Search vacancy (g-926@ya.ru)"
    )
    @GET("/api/vacancies")
    suspend fun searchVacancies(@QueryMap queryParams: Map<String, String>): VacanciesResponse

}