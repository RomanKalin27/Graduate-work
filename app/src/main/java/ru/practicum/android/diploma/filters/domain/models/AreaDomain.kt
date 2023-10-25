package ru.practicum.android.diploma.filters.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AreaDomain(
    val id: String? = "",
    val name: String? = "",
    val parentId: String? = "",
) {
    companion object {
        val emptyArea = AreaDomain(name = "")
    }
}
