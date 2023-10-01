package ru.practicum.android.diploma.search.data.dto.response_models

data class Salary(
    val currency: String,
    val from: Int?,
    val gross: Boolean,
    val to: Int?
)
