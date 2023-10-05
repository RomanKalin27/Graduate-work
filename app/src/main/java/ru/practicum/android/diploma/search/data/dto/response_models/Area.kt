package ru.practicum.android.diploma.search.data.dto.response_models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Area(
    val name: String,
) {
    companion object {
        val emptyArea = Area(name = "")
    }
}
/**
 как вариант:



*/
