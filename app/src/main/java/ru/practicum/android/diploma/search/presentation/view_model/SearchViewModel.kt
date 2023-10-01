package ru.practicum.android.diploma.search.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _vacancies = MutableStateFlow<VacanciesResponse?>(null)
    val vacancies: StateFlow<VacanciesResponse?> = _vacancies

    fun searchVacancies(queryParams: Map<String, String>) {
        viewModelScope.launch {
            val result = searchInteractor.execute(queryParams)
            result.collect {
                _vacancies.value = it
            }
        }
    }

}