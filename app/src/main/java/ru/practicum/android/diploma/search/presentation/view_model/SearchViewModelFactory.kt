package ru.practicum.android.diploma.search.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor

class SearchViewModelFactory(
    val searchInteractor: SearchInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchInteractor = searchInteractor
        ) as T
    }
}