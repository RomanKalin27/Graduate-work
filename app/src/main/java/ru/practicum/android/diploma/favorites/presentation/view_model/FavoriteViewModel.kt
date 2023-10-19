package ru.practicum.android.diploma.favorites.presentation.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.vacancy.domain.impl.DetailVacancyInteractor

class FavoriteViewModel(private val favoriteVacancyInteractor: DetailVacancyInteractor) :
    ViewModel() {
//
//    private val contentStateLiveData = MutableLiveData<FavoritesScreenState>()
//    fun observeContentState(): LiveData<FavoritesScreenState> = contentStateLiveData
//    fun fillData() {
//        viewModelScope.launch {
//            favoriteVacancyInteractor
//                .getFavouriteVacancies()
//                .collect { vacancyList ->
//                    processResult(vacancyList)
//                }
//        }
//    }
//    private fun processResult(vacancyList: List<Vacancy>) {
//
//        when {
//            vacancyList.isEmpty() -> {
//                contentStateLiveData.value = FavoritesScreenState.Empty
//            }
//            else -> {
//                contentStateLiveData.value = FavoritesScreenState.Content(vacancyList)
//            }
//        }
//    }
//    fun removeVacancy(id: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            favoriteVacancyInteractor
//                .deleteFavouriteVacancyById(vacancyId = id)
//        }
//    }
}