package ru.practicum.android.diploma.vacancy.data.network.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.dto.response_models.Area
import ru.practicum.android.diploma.search.data.dto.response_models.Employer
import ru.practicum.android.diploma.search.data.dto.response_models.Salary
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.ContactsDto
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.EmploymentDto
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.ExperienceDto
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.KeySkillDto
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.ScheduleDto

data class VacancyDetailModelDTO(
    val id: String,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    val schedule: ScheduleDto?,
    val description: String?,
    @SerializedName("key_skills")
    val keySkills: List<KeySkillDto>?,
    val contacts: ContactsDto?,
    val area: Area?,
    val salary: Salary?,
    val name: String?,
    val employer: Employer?,
    @SerializedName("alternate_url")
    val alternateUrl: String?,
)
