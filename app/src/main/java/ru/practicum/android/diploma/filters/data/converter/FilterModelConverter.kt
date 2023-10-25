package ru.practicum.android.diploma.filters.data.converter

import ru.practicum.android.diploma.filters.data.dto.models.IndustryDTO
import ru.practicum.android.diploma.filters.domain.models.AreaDomain
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.filters.domain.models.Regions

class FilterModelConverter {

    fun industryDTOListToIndustryList(list: List<IndustryDTO>): List<Industry> {
        return list.flatMap { it.industries ?: emptyList() }
            .map {
                Industry(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    industries = emptyList()
                )
            }
            .sortedBy { it.name }
    }

    fun regionDTOListToAreaList(list: List<Regions>): List<AreaDomain> {
        val resultList = list.map {
            AreaDomain(
                id = it.id ?: "",
                name = it.name ?: "",
                parentId = it.parentId ?: ""
            )
        }.sortedBy { it.id }
        return (resultList + list.flatMap { it.areas }).sortedBy { it.id }
            .filterNot { it.parentId == "1001" }
    }
}
