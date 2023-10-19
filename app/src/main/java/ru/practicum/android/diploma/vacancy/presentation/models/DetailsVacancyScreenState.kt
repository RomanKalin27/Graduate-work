package ru.practicum.android.diploma.vacancy.presentation.models

enum class DetailsVacancyScreenState {
    NO_INTERNET,
    LOADING,
    /*Стоит ли CONTENT обосабливать? В updateUI он обрабатывается
    в комплекте с FAVORITE и UNFAVORITE*/

    //   CONTENT,
    ERROR,
    FAVORITE,
    UNFAVORITE
}