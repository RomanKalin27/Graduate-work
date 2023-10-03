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

data class AreasDto(
    val id:String,
    val name:String,
    val areas:List<RegionsDto>
)
data class RegionsDto(
    val id:String,
    val name:String,
    @SerializedName("parent_id")
    val parentId:String
)
data class CountryDto(
    val url: String,
    val id:String,
    val name:String
)
data class IndustryDto(
    val id:String,
    val industries:List<IndustriesDto>,
    val name:String
)
data class IndustriesDto(
    val id:String,
    val name: String
)*/
