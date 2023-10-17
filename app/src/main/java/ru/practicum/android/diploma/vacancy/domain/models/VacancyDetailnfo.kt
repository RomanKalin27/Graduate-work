package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyDetailnfo(
    val id: String,
    val title: String = "",
    val area: String = "",
    val date: String = "",
    val company: String = "",
    val salary: String = "",
    val logo: String = "",
    val experience: String = "",
    val employment: String = "",
    val schedule: String = "",
    val description: String = "",
    val keySkills: String = "",
    val contactName: String = "",
    val contactEmail: String = "",
    val contactPhones: List<String> = emptyList(),
    val contactComment: String = "",
    val alternateUrl: String? = "",
    val isInFavorite: Boolean = false
)
