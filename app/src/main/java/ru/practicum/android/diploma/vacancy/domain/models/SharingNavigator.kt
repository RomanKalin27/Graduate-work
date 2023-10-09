package ru.practicum.android.diploma.vacancy.domain.models

interface SharingNavigator {
    fun shareVacancyUrl(vacancyUrl: String)
    fun sharePhone(phone: String)
    fun shareEmail(email: String)
}