package ru.practicum.android.diploma.filters.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Areas(
    val id: String,
    val name: String,
    val areas: List<Regions>,
) {
    companion object {
        val emptyArea = Areas(id = "", name = "", emptyList())
    }
}
