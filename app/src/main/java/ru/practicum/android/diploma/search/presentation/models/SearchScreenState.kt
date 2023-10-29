package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchScreenState(
    val result: SearchVacancyResult?,
    val isFilterOn: Boolean
)