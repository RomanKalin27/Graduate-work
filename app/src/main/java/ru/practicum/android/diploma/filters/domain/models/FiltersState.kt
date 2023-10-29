package ru.practicum.android.diploma.filters.domain.models


class FiltersState(
    var country: String?,
    var region: String?,
    var industry: String?,
    var lowestSalary: String?,
    var removeNoSalary: Boolean,
)