package ru.practicum.android.diploma.search.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.common.utils.Constants.CLICK_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.Constants.SEARCH_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.core.root.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.filters.presentation.ui.FiltersFragment.Companion.SET_FILTERS_KEY
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.presentation.models.SearchUIState
import ru.practicum.android.diploma.search.presentation.rv.VacancyAdapter
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.ui.VacancyFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private val viewModel by viewModel<SearchViewModel>()
    private val vacancyList = ArrayList<Vacancy>()
    private lateinit var onVacancyClickDebounce: (Vacancy) -> Unit
    private val vacancyAdapter = VacancyAdapter(vacancyList)
    private var timerJob: Job? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel.isFilterOn()
    }

    override fun onResume() {
        super.onResume()
        setupDefaultUI()
        observeViewModel()
        setFragmentResultListener(SET_FILTERS_KEY) { _, bundle ->
            if (!binding.searchEditText.text.isNullOrEmpty()) {
                viewModel.searchVacancies(binding.searchEditText.text.toString())
            }
        }
    }

    private fun setupViews() {
        binding.iconFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filtersFragment)
        }
        binding.iconSearch.setOnClickListener {
            binding.searchEditText.text.clear()
        }
        vacancyAdapter.itemClickListener = { position, vacancy ->
            (activity as? RootActivity)?.animateBottomNavigationView()
            onVacancyClickDebounce(vacancy)
        }
        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false
        ) { item ->
            navigateToVacancyDetail(item.id)
        }
        setupDefaultUI()
        setupTextWatcher()
        setupRecyclerView()
    }

    private fun debounceSearch(p0: String) {
        timerJob?.cancel()
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            viewModel.searchVacancies(p0)
        }
    }

    private fun setupDefaultUI() {
        with(binding) {
            searchPlaceholder.setImageResource(R.drawable.placeholder_search)
            if (searchEditText.text.isNullOrEmpty()) {
                clearUI()
            }
        }
        println("Test")
    }

    private fun observeViewModel() {
        viewModel.searchVacancyResult.observe(viewLifecycleOwner) { state ->
            binding.iconSearch.setImageResource(R.drawable.ic_search)


            when (state) {
                is SearchVacancyResult.Error -> updateUI(SearchUIState.CONNECTION_ERROR)
                SearchVacancyResult.EmptyResult -> updateUI(SearchUIState.EMPTY_SEARCH)
                SearchVacancyResult.NoInternet -> updateUI(SearchUIState.NO_INTERNET)
                is SearchVacancyResult.Success -> showVacancy(state.response.vacancies)
                SearchVacancyResult.Loading -> updateUI(SearchUIState.LOADING)
                is SearchVacancyResult.StartScreen -> setFilterIcon(state.isFiltersOn)
            }
        }
    }

    private fun updateUI(searchUIState: SearchUIState) {
        with(binding) {
            // Скрываем searchPlaceholder и другие плейсхолдеры
            searchPlaceholder.isVisible = false
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            recyclerView.isVisible = false
            chip.isVisible = false
            vacancyList.clear()

            // Обработка конкретного состояния
            when (searchUIState) {
                SearchUIState.CONNECTION_ERROR -> placeholderServerError.isVisible = true
                SearchUIState.EMPTY_SEARCH -> {
                    placeholderNoVacancies.isVisible = true
                    chip.visibility = View.VISIBLE
                    val message = getString(R.string.no_vacansy)
                    chip.text = message
                }
                SearchUIState.NO_INTERNET -> noInternetPlaceholder.isVisible = true
                SearchUIState.LOADING -> progressBar.isVisible = true
            }
            iconSearch.setImageResource(R.drawable.ic_search)
            vacancyAdapter.notifyDataSetChanged()
        }
        updateIconBasedOnText()
    }
    private fun setFilterIcon(isFilterOn: Boolean){
        if(isFilterOn){
            binding.iconFilter.setImageResource(R.drawable.ic_filter_on)
        } else {
            binding.iconFilter.setImageResource(R.drawable.ic_filter_off)
        }
    }

    private fun showVacancy(items: List<Vacancy>) {
        with(binding) {
            recyclerView.isVisible = true
            searchPlaceholder.isVisible = false
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            progressBar.isVisible = false

            val isEmptyResult = items.isEmpty()
            if (isEmptyResult) {
                chip.visibility = View.VISIBLE
                val message = getString(R.string.no_vacansy)
                chip.text = message
            } else {
                chip.visibility = View.VISIBLE
                val vacancyCount = items.size
                val message = resources.getQuantityString(
                    R.plurals.search_result_count, vacancyCount, vacancyCount
                )
                chip.text = message
            }
        }
        vacancyList.clear()
        vacancyList.addAll(items)
        vacancyAdapter.notifyDataSetChanged()
        updateIconBasedOnText()
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = vacancyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun clearUI() {
        with(binding) {
            searchPlaceholder.isVisible = true
            recyclerView.isVisible = false
            progressBar.isVisible = false
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            chip.isVisible = false
            iconSearch.setImageResource(R.drawable.ic_search)
        }
        vacancyList.clear()
        vacancyAdapter.notifyDataSetChanged()
        timerJob?.cancel()
        updateIconBasedOnText()
    }

    private fun updateIconBasedOnText() {
        if (binding.searchEditText.text.isNullOrEmpty()) {
            binding.iconSearch.setImageResource(R.drawable.ic_search)
        } else {
            binding.iconSearch.setImageResource(R.drawable.ic_clear)
        }
    }

    private fun setupTextWatcher() {
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearUI()
                } else {
                    binding.iconSearch.setImageResource(R.drawable.ic_clear)
                    debounceSearch(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                updateIconBasedOnText()
            }
        }
        binding.searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun navigateToVacancyDetail(item: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment, VacancyFragment.createArgs(item)
        )
    }
}