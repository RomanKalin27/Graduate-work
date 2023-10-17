package ru.practicum.android.diploma.vacancy.data.network.dto.help_models

data class ContactsDto(
    val email: String? = "",
    val name: String? = "",
    val phones: List<Phone>? = emptyList(),
)