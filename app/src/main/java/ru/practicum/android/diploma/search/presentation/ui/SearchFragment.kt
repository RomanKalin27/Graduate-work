package ru.practicum.android.diploma.search.presentation.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
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
    private var savedText = ""
    private var doSearchAgain = true
    private var doSearch = true
    //private var recyclerState: Parcelable? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel.isFilterOn()
        observeViewModel()
        setupDefaultUI()
        setFragmentResultListener(SET_FILTERS_KEY) { _, bundle ->
            if (!binding.searchEditText.text.isNullOrEmpty()) {
                viewModel.searchVacancies(binding.searchEditText.text.toString())
            }
        }
    }

    private fun setupViews() {
        binding.iconFilter.setOnClickListener {
            doSearchAgain = false
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
            doSearchAgain = false
            navigateToVacancyDetail(item.id)
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = vacancyAdapter.itemCount
                    if (pos >= itemsCount - 1) {
                        if (savedText != "") {
                            binding.progressBar.visibility = View.VISIBLE
                            viewModel.currentPageInc()
                            viewModel.searchVacancies(savedText)
                        }
                    }
                }
            }
        })
        setupDefaultUI()
        setupTextWatcher()
        setupRecyclerView()
    }

    private fun debounceSearch(p0: String) {
        timerJob?.cancel()
        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            viewModel.newSearchVacancies(p0)
            binding.progressBarLoader.visibility = View.VISIBLE
            binding.searchEditText.isFocusable = false
            binding.searchEditText.isFocusableInTouchMode = true
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun setupDefaultUI() {
        with(binding) {
            searchPlaceholder.setImageResource(R.drawable.placeholder_search)
            if (searchEditText.text.isNullOrEmpty()) {
                clearUI()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.searchVacancyResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchVacancyResult.Error -> updateUI(SearchUIState.CONNECTION_ERROR)
                SearchVacancyResult.EmptyResult -> updateUI(SearchUIState.EMPTY_SEARCH)
                SearchVacancyResult.NoInternet -> updateUI(SearchUIState.NO_INTERNET)
                is SearchVacancyResult.Success -> {
                    viewModel.maxPagesSet(state.response.pages)
                    viewModel.isNextPageLoading = true
                    showVacancy(state.response.vacancies)}
                SearchVacancyResult.Loading -> updateUI(SearchUIState.LOADING)
                is SearchVacancyResult.StartScreen -> setFilterIcon(state.isFiltersOn)
            }
            doSearch = false
        }
    }

    private fun updateUI(searchUIState: SearchUIState) {
        with(binding) {
            searchPlaceholder.isVisible = false
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            progressBarLoader.isVisible = false
            recyclerView.isVisible = false
            chip.isVisible = false
            vacancyList.clear()

            when (searchUIState) {
                SearchUIState.CONNECTION_ERROR -> {
                    placeholderServerError.isVisible = true
                    viewModel.allowSearch()
                }
                SearchUIState.EMPTY_SEARCH -> {
                    viewModel.allowSearch()
                    placeholderNoVacancies.isVisible = true
                  //2023.11.28  progressBarLoader.isVisible = false
                    chip.visibility = View.VISIBLE
                    val message = getString(R.string.no_vacansy)
                    chip.text = message
                }

                SearchUIState.NO_INTERNET -> {
                    noInternetPlaceholder.isVisible = true
                    viewModel.allowSearch()
                }
                SearchUIState.LOADING -> {
                    progressBarLoader.isVisible = true
                }

            }
            vacancyAdapter.notifyDataSetChanged()
        }
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
            progressBar.visibility = View.GONE
            progressBarLoader.visibility = View.GONE
            val isEmptyResult = items.isEmpty()
            if (isEmptyResult) {
                chip.visibility = View.VISIBLE
                val message = getString(R.string.no_vacansy)
                chip.text = message
            } else {
                chip.visibility = View.VISIBLE
                val vacancyCount = viewModel.maxPages * 20
                val message = resources.getQuantityString(
                    R.plurals.search_result_count, vacancyCount, vacancyCount
                )
                chip.text = message
            }
        }
        vacancyList.addAll(items)
        vacancyAdapter.notifyDataSetChanged()
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
            progressBarLoader.isVisible = false
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            chip.isVisible = false
        }
        viewModel.clearCurrentPage()
        vacancyList.clear()
        vacancyAdapter.notifyDataSetChanged()
        timerJob?.cancel()
    }

    private fun setupTextWatcher() {
        binding.searchEditText.addTextChangedListener {
            changeSearchField()
            savedText = binding.searchEditText.text.toString()
            if (!binding.searchEditText.text.isNullOrEmpty()) {
                if(doSearchAgain) {
                    debounceSearch(binding.searchEditText.text.toString())
                }
            }
            doSearchAgain = true
        }
        binding.searchEditText.setOnFocusChangeListener { _, _ ->
            changeSearchField()
        }
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!binding.searchEditText.text.isNullOrEmpty()) {
                    viewModel.searchVacancies(binding.searchEditText.text.toString())
                }
                binding.searchEditText.isFocusable = false
                binding.searchEditText.isFocusableInTouchMode = true
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun changeSearchField() {
        if (binding.searchEditText.text.isEmpty()) {
            clearUI()
            savedText = ""
            binding.iconSearch.setImageResource(R.drawable.ic_search)
            binding.iconSearch.isEnabled = false
        } else {
            if (!binding.searchEditText.isFocused) {
                binding.iconSearch.setImageResource(R.drawable.ic_search)
                binding.iconSearch.isEnabled = false
            } else {
                binding.iconSearch.setImageResource(R.drawable.ic_clear)
                binding.iconSearch.isEnabled = true
            }
        }
    }

    private fun navigateToVacancyDetail(item: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment, VacancyFragment.createArgs(item)
        )
    }
}