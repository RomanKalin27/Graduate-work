package ru.practicum.android.diploma.search.data.dto.response_models


data class Area(
    val id: String? = "",
    val name: String? = "",
    val url: String? = "",
) {
    companion object {
        val emptyArea = Area(name = "")
    }
}
