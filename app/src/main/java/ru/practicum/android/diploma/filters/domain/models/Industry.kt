package ru.practicum.android.diploma.filters.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Industry(
    val id: String = "",
    val industries: List<Industries> = emptyList(),
    val name: String = "",
){
    companion object{
        val emptyIndustry = Industry("", emptyList(),"")
    }
}