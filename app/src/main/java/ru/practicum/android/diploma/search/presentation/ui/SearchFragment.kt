package ru.practicum.android.diploma.search.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
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

class SearchFragment : Fragment() {
    private val viewModel by viewModel<SearchViewModel>()
    private val vacancyList = ArrayList<Vacancy>()
    private val handler = Handler(Looper.getMainLooper())
    private val vacancySearchRunnable =
        Runnable { viewModel.searchVacancies(binding.searchEditText.text.toString()) }
    private lateinit var onVacancyClickDebounce: (Vacancy) -> Unit
    private val vacancyAdapter = VacancyAdapter(vacancyList)
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        onVacancyClickDebounce = debounce<Vacancy>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { item ->
            navigateToVacancyDetail(item.id)
        }
    }

    override fun onResume() {
        super.onResume()
        setupDefaultUI()
        setFragmentResultListener(SET_FILTERS_KEY)
        { _, bundle ->
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

        setupDefaultUI()
        setupTextWatcher()
        setupRecyclerView()
    }

    private fun setupDefaultUI() {
        with(binding) {
            searchPlaceholder.setImageResource(R.drawable.placeholder_search)
            viewModel.isFiltersOn(iconFilter)
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
            }
        }
    }

    private fun updateUI(searchUIState: SearchUIState) {
        with(binding) {
            recyclerView.visibility = View.GONE

            when (searchUIState) {
                SearchUIState.CONNECTION_ERROR -> {
                    searchPlaceholder.setImageResource(R.drawable.placeholder_sad)
                    searchPlaceholderText.setText(R.string.server_error)
                    vacancyList.clear()
                }

                SearchUIState.EMPTY_SEARCH -> {
                    searchPlaceholder.setImageResource(R.drawable.placeholder_cat)
                    searchPlaceholderText.setText(R.string.error_no_vacancies)
                    vacancyList.clear()
                }

                SearchUIState.NO_INTERNET -> {
                    searchPlaceholder.setImageResource(R.drawable.placeholder_skull)
                    searchPlaceholderText.setText(R.string.error_no_internet)
                    vacancyList.clear()
                }

                SearchUIState.LOADING -> TODO()
            }
            searchPlaceholder.visibility = View.VISIBLE

            iconSearch.setImageResource(R.drawable.ic_search)
            vacancyList.clear()

            vacancyAdapter.notifyDataSetChanged()
            handler.removeCallbacks(vacancySearchRunnable)
        }
    }

    private fun showVacancy(items: List<Vacancy>) {
        with(binding) {
            recyclerView.visibility = View.VISIBLE
            searchPlaceholder.visibility = View.GONE
        }
        vacancyList.clear()
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
            searchPlaceholder.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            iconSearch.setImageResource(R.drawable.ic_search)
        }
        vacancyList.clear()
        vacancyAdapter.notifyDataSetChanged()
        handler.removeCallbacks(vacancySearchRunnable)
    }

    private fun setupTextWatcher() {
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearUI()
                } else {
                    binding.iconSearch.setImageResource(R.drawable.ic_clear)
                    handler.removeCallbacks(vacancySearchRunnable)
                    handler.postDelayed(vacancySearchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun navigateToVacancyDetail(item: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(item)
        )
    }
}