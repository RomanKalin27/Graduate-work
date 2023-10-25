package ru.practicum.android.diploma.search.presentation.view_model

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private var currentPage = 1
    var maxPages = 0
    var isNextPageLoading = true
    private val _searchVacancyResult: MutableLiveData<SearchVacancyResult> = MutableLiveData()
    val searchVacancyResult: LiveData<SearchVacancyResult> = _searchVacancyResult

    fun searchVacancies(query: String) {
        viewModelScope.launch {
            if (isNextPageLoading) {
                if (currentPage != maxPages) {
                    isNextPageLoading = false
                    searchInteractor.execute(query, currentPage).collect { result ->
                        _searchVacancyResult.value = result
                    }
                }
            }
        }
    }

    fun isFiltersOn(iconFilter: ImageView) {
        if (searchInteractor.isFiltersOn()) {
            iconFilter.setImageResource(R.drawable.ic_filter_on)
        } else {
            iconFilter.setImageResource(R.drawable.ic_filter_off)
        }
    }

    fun currentPageInc() {
        currentPage += 1
    }

    fun MaxPagesSet(max: Int) {
        maxPages = max
    }

    fun clearCurrentPage(){
        currentPage = 1
    }
}