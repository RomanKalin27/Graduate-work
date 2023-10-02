package ru.practicum.android.diploma.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchResult

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _searchResult: MutableLiveData<SearchResult> = MutableLiveData()
    val searchResult: LiveData<SearchResult> = _searchResult

    fun searchVacancies(queryParams: Map<String, String>) {
        viewModelScope.launch {
            searchInteractor.execute(queryParams).collect { result ->
                _searchResult.value = result
            }
        }
    }
}