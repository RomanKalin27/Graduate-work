package ru.practicum.android.diploma.filters.data.dto.models

import com.google.gson.annotations.SerializedName

data class RegionsDTO(
    val id: String,
    val name: String,
    @SerializedName("parent_id")
    val parentId: String,
)