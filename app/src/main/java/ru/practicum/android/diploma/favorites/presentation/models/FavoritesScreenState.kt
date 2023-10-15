package ru.practicum.android.diploma.favorites.presentation.models

import ru.practicum.android.diploma.db.domain.models.Vacancy


sealed interface FavoritesScreenState {
    object Empty: FavoritesScreenState
    data class Content(val list: List<Vacancy>): FavoritesScreenState
}