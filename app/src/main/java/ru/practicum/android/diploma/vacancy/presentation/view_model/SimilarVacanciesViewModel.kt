package ru.practicum.android.diploma.vacancy.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.vacancy.domain.impl.SimilarVacancyInteractor

class SimilarVacanciesViewModel(private val similarVacancyInteractor: SimilarVacancyInteractor) :
    ViewModel() {
    private val _similarVacanciesResult: MutableLiveData<SearchVacancyResult> = MutableLiveData()
    val similarVacanciesResult: LiveData<SearchVacancyResult> = _similarVacanciesResult


    fun searchVacancies(id: String) {
        _similarVacanciesResult.value = SearchVacancyResult.Loading
        viewModelScope.launch {
            similarVacancyInteractor.execute(id).collect { result ->
                _similarVacanciesResult.value = result
            }
        }
    }
}