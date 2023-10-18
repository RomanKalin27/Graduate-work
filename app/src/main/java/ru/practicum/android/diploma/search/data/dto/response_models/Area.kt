package ru.practicum.android.diploma.search.data.dto.response_models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Area(
    val id: String? = "",
    val name: String? = "",
    @SerializedName("parent_id")
    val parentId: String? = "",
) {
    companion object {
        val emptyArea = Area(name = "")
    }
}
