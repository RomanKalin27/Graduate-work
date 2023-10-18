package ru.practicum.android.diploma.filters.data.dto.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ru.practicum.android.diploma.search.data.dto.response_models.Area

@Serializable
data class RegionsDTO(
    val id: String?,
    val name: String?,
    @SerializedName("parent_id")
    val parentId: String?,
    val areas: List<Area>,
)