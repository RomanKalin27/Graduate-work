package ru.practicum.android.diploma.favorites.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.db.domain.api.VacancyDbInteractor
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.favorites.presentation.models.FavoritesScreenState

class FavoriteViewModel(private val favoriteVacancyInteractor: VacancyDbInteractor) : ViewModel() {

    private val contentStateLiveData = MutableLiveData<FavoritesScreenState>()
    fun observeContentState(): LiveData<FavoritesScreenState> = contentStateLiveData
    fun fillData() {
        viewModelScope.launch {
            favoriteVacancyInteractor
                .getFavouriteVacancies()
                .collect { vacancyList ->
                    processResult(vacancyList)
                }
        }
    }
    private fun processResult(vacancyList: List<Vacancy>) {

        when {
            vacancyList.isEmpty() -> {
                contentStateLiveData.value = FavoritesScreenState.Empty
            }
            else -> {
                contentStateLiveData.value = FavoritesScreenState.Content(vacancyList)
            }
        }
    }
    fun removeVacancy(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteVacancyInteractor
                .deleteFavouriteVacancyById(vacancyId = id)
        }
    }
}