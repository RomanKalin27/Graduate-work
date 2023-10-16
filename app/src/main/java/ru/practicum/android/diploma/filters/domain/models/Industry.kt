package ru.practicum.android.diploma.filters.domain.models

data class Industry(
    val id: String = "",
    val industries: List<Industries> = emptyList(),
    val name: String = "",
)