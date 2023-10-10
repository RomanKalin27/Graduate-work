package ru.practicum.android.diploma.filters.data.converter

import ru.practicum.android.diploma.filters.data.dto.models.IndustryDTO
import ru.practicum.android.diploma.filters.domain.models.Industry

class FilterModelConverter (){

    fun industryDTOListToIndustryList(list: List<IndustryDTO>): List<Industry> {
        return list.flatMap { it.industries ?: emptyList() }
            .map {
                Industry(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    industries = emptyList())
            }
            .sortedBy { it.name }
    }
}
