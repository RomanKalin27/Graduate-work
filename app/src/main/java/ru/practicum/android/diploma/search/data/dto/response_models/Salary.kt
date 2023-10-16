package ru.practicum.android.diploma.search.data.dto.response_models

data class Salary(
    val currency: String? = "",
    val from: Int? = 0,
    val gross: Boolean? = false,
    val to: Int? = 0,
)
