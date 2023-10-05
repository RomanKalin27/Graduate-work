package ru.practicum.android.diploma.search.data.dto.response_models

data class VacancyItem(
    val area: Area?,
    val name: String? = "",
    val salary: Salary? = Salary(),
    val employer: Employer?,
)

