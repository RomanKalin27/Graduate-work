package ru.practicum.android.diploma.search.data.network

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.db.data.entity.VacancyFullInfoEntity
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.data.dto.response_models.Salary
import ru.practicum.android.diploma.search.domain.models.ConvertedResponse
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.data.network.dto.VacancyDetailModelDTO
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.KeySkillDto
import ru.practicum.android.diploma.vacancy.data.network.dto.help_models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

class ModelConverter(private val context: Context) {
    fun convertVacanciesResponse(response: VacanciesResponse): ConvertedResponse {
        val vacancies = response.items.map { item ->
            Vacancy(
                id = item.id,
                logoUrl = item.employer?.logo_urls?.url240.toString(),
                title = item.name,
                company = item.employer?.name ?: "",
                salary = convertSalary(item.salary)
            )
        }
        return ConvertedResponse(vacancies, response.pages)
    }

    fun convertSalary(salary: Salary?): String {
        if (salary == null) return context.getString(R.string.salary_not_specified)
        val result = StringBuilder()

        salary.from?.let {
            result.append(context.getString(R.string.from))
            result.append(" ")
            result.append(it)
            result.append(" ")
        }

        salary.to?.let {
            result.append(context.getString(R.string.to))
            result.append(" ")
            result.append(it)
        }

        val currencySymbol = when (salary.currency) {
            "AZN" -> "₼"
            "BYR" -> "Br"
            "EUR" -> "€"
            "GEL" -> "₾"
            "KGS" -> "сом"
            "KZT" -> "₸"
            "RUR" -> "₽"
            "UAH" -> "₴"
            "USD" -> "$"
            "UZS" -> "so'm"
            else -> salary.currency
        }
        result.append(" ")
        result.append(currencySymbol)

        return result.toString()
    }

    fun mapDetails(details: VacancyDetailModelDTO): VacancyDetailnfo {
        return details.mapToDetails()
    }

    private fun VacancyDetailModelDTO.mapToDetails(): VacancyDetailnfo {
        return VacancyDetailnfo(
            id = id,
            description = description.orEmpty(),
            experience = experience?.name.orEmpty(),
            employment = employment?.name.orEmpty(),
            schedule = schedule?.name.orEmpty(),
            area = area?.name.orEmpty(),
            salary = convertSalary(salary),
            company = employer?.name.orEmpty(),
            logo = employer?.logo_urls?.url240.orEmpty(),
            title = name.orEmpty(),
            contactEmail = contacts?.email.orEmpty(),
            contactName = contacts?.name.orEmpty(),
            keySkills = keySkillsToString(keySkills),
            contactPhones = createPhones(contacts?.phones),
            contactComment = contacts?.phones?.getOrNull(0)?.comment.orEmpty(),
            alternateUrl = alternateUrl.orEmpty()
        )
    }

    fun toFullInfoEntity(vacancy: VacancyDetailnfo): VacancyFullInfoEntity {
        return VacancyFullInfoEntity(
            id = vacancy.id,
            experience = vacancy.experience,
            employment = vacancy.employment,
            schedule = vacancy.schedule,
            description = vacancy.description,
            keySkills = vacancy.keySkills,
            area = vacancy.area,
            salary = vacancy.salary,
            date = vacancy.date,
            company = vacancy.company,
            logo = vacancy.logo,
            title = vacancy.title,
            contactEmail = vacancy.contactEmail,
            contactName = vacancy.contactName,
            contactComment = vacancy.contactComment,
            contactPhones = Json.encodeToString(vacancy.contactPhones),
            alternativeUrl = vacancy.alternateUrl
        )
    }

    fun mapToVacancies(entities: List<VacancyFullInfoEntity>): List<Vacancy> {
        return entities.map { toVacancy(it) }
    }

    private fun toVacancy(vacancyEntity: VacancyFullInfoEntity): Vacancy {
        return with(vacancyEntity) {
            Vacancy(
                id = id,
                logoUrl = logo,
                title = title,
                company = company,
                salary = salary,
                area = area,
                date = date,
            )
        }
    }

    fun entityToModel(vacancyEntity: VacancyFullInfoEntity?): VacancyDetailnfo? {
        if (vacancyEntity != null) {
            return with(vacancyEntity) {
                VacancyDetailnfo(
                    id = id,
                    experience = experience,
                    employment = employment,
                    schedule = schedule,
                    description = description,
                    keySkills = keySkills,
                    area = area,
                    salary = salary,
                    date = date,
                    company = company,
                    logo = logo,
                    title = title,
                    contactEmail = contactEmail,
                    contactName = contactName,
                    contactComment = contactComment,
                    contactPhones = Json.decodeFromString(contactPhones),
                    alternateUrl = alternativeUrl,
                    isInFavorite = true
                )
            }
        } else {
            return null
        }
    }

    private fun keySkillsToString(keySkills: List<KeySkillDto>?): String {
        return keySkills?.map { "• ${it.name}" }?.joinToString("\n") ?: ""
    }

    private fun createPhones(phones: List<Phone?>?): List<String> {
        if (phones == null) {
            return emptyList()
        }

        val phoneList = mutableListOf<String>()
        for (phone in phones) {
            phone?.let {
                val number = "+${it.country} (${it.city}) ${it.number}"
                phoneList.add(number)
            }
        }

        return phoneList
    }

}