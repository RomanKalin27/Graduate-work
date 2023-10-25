package ru.practicum.android.diploma.filters.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Regions(
    val id: String?,
    val name: String?,
    val parentId: String?,
    val areas: List<AreaDomain>,
)
