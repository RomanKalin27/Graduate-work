package ru.practicum.android.diploma.search.data.dto.response_models

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("240")
    val url240: String? = "",
    val original: String? = "",
)