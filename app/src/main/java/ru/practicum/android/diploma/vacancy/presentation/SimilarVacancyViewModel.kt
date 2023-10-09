package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.SearchState
import ru.practicum.android.diploma.vacancy.domain.models.VacancyInteractor

class SimilarVacancyViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val resourceProvider: ResourceProvider,
): ViewModel()  {

    private val vacanciesList = mutableListOf<Vacancy>()

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    fun getSimilarVacanciesById(vacancyId: String){
        _state.postValue(SearchState.FirstLoading)
        viewModelScope.launch {
            vacancyInteractor.getSimilarVacanciesById(vacancyId)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(foundVacancies: List<Vacancy>?, errorMessage: String?) {
        if (foundVacancies != null) {
            vacanciesList.addAll(foundVacancies)
        }
        when {
            errorMessage != null ->
                _state.postValue(SearchState.Error(resourceProvider.getString(R.string.error_no_internet)))

            vacanciesList.isEmpty() ->
                _state.postValue(SearchState.Empty(resourceProvider.getString(R.string.no_vacansy)))

            else ->
                _state.postValue(SearchState.VacancyContent(
                    vacancies = vacanciesList,
                    foundValue = vacanciesList[0].found
                ))
        }
    }
}