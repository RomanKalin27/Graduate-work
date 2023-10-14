package ru.practicum.android.diploma.filters.data.dto.models

import kotlinx.serialization.Serializable


@Serializable
data class AreasDTO(
    val id: String,
    val name: String,
    val areas: List<RegionsDTO>,
) {
    companion object {
        val emptyArea = AreasDTO(id = "", name = "", emptyList())
    }
}