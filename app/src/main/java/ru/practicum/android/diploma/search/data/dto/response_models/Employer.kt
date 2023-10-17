package ru.practicum.android.diploma.search.data.dto.response_models

import com.google.gson.annotations.SerializedName


data class Employer(
    val id: String? = "",
    val logo_urls: LogoUrls? = LogoUrls(),
    val name: String? = "",
    @SerializedName("alternate_url")
    val alternateUrl: String? = "",
)
