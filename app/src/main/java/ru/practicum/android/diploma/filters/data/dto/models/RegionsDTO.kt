package ru.practicum.android.diploma.filters.data.dto.models

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.dto.response_models.Area

data class RegionsDTO(
    val id: String?,
    val name: String?,
    @SerializedName("parent_id")
    val parentId: String?,
    val areas: List<Area>,
)