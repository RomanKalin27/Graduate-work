package ru.practicum.android.diploma.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.search.presentation.models.SearchScreenState

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private var currentPage = 1
    var maxPages = 0
    var isNextPageLoading = true
    private var isFiltersOn = false
    private var vacanyResult: SearchVacancyResult? = null
    private val _searchVacancyResult: MutableLiveData<SearchScreenState> = MutableLiveData()
    val searchVacancyResult: LiveData<SearchScreenState> = _searchVacancyResult

    fun searchVacancies(query: String) {
        viewModelScope.launch {
            if (isNextPageLoading) {
                if (currentPage != maxPages) {
                    isNextPageLoading = false
                    searchInteractor.execute(query, currentPage).collect { result ->
                        vacanyResult = result
                        _searchVacancyResult.postValue(SearchScreenState(result, isFiltersOn))
                    }
                }
            }
        }
    }

    fun newSearchVacancies(query: String) {
        vacanyResult = SearchVacancyResult.Loading
        _searchVacancyResult.postValue(SearchScreenState(vacanyResult, isFiltersOn))
        viewModelScope.launch {
            searchInteractor.execute(query, currentPage).collect { result ->
                vacanyResult = result
                _searchVacancyResult.postValue(SearchScreenState(result, isFiltersOn))
            }
        }
    }
    fun isFilterOn(){
        isFiltersOn = searchInteractor.isFiltersOn()
        _searchVacancyResult.postValue(SearchScreenState(vacanyResult, isFiltersOn))
    }
    fun currentPageInc() {
        currentPage += 1
    }

    fun maxPagesSet(max: Int) {
        maxPages = max
    }

    fun clearCurrentPage(){
        currentPage = 1
    }

    fun allowSearch(){
        isNextPageLoading = true
    }
}