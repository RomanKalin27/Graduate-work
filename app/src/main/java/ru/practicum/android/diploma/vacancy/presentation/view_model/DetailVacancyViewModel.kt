package ru.practicum.android.diploma.vacancy.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.vacancy.domain.impl.DetailVacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo

class DetailVacancyViewModel(private val detailVacancyInteractor: DetailVacancyInteractor) :
    ViewModel() {

    private val _detailVacancyResult: MutableLiveData<DetailVacancyResult> = MutableLiveData()
    val detailVacancyResult: LiveData<DetailVacancyResult> = _detailVacancyResult
    private var isFavourite = false
    private val _shareUrl = MutableLiveData<String?>()
    val shareUrl: LiveData<String?> get() = _shareUrl

    fun showDetailVacancy(id: String) {
        viewModelScope.launch {
            detailVacancyInteractor.getDetailVacancyById(id).collect { result ->
                _detailVacancyResult.value = result
                if (result is DetailVacancyResult.Success) {
                    _shareUrl.value = result.response.alternateUrl
                }
            }
        }
    }

    fun clickToFavoriteButton(vacancy: VacancyDetailnfo) {
        viewModelScope.launch(Dispatchers.IO) {
            isFavourite = detailVacancyInteractor.checkIfVacancyInFavorite(vacancy.id)
            if (isFavourite) {
                detailVacancyInteractor.removeVacancyFromFavorites(vacancy.id).collect {
                    _detailVacancyResult.postValue(DetailVacancyResult.NoFavorite)
                }
            } else {
                detailVacancyInteractor.addVacancyToFavorites(vacancy).collect {
                    _detailVacancyResult.postValue(DetailVacancyResult.AddedToFavorite)

                }
            }

        }
    }
     fun checkFavorite(vacancy: VacancyDetailnfo){
        viewModelScope.launch(Dispatchers.IO) {
            isFavourite = detailVacancyInteractor.checkIfVacancyInFavorite(vacancy.id)
            if (isFavourite) {
                _detailVacancyResult.postValue(DetailVacancyResult.AddedToFavorite)
            }
            else {
                _detailVacancyResult.postValue(DetailVacancyResult.NoFavorite)
            }
        }
    }


}