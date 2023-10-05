package ru.practicum.android.diploma.search.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.data.dto.response_models.VacancyItem
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.search.presentation.models.SearchUIState
import ru.practicum.android.diploma.search.presentation.rv.VacancyAdapter
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel

class SearchFragment : Fragment() {
    private val viewModel by viewModel<SearchViewModel>()
    private val vacancyList = ArrayList<VacancyItem>()
    private val vacancyAdapter = VacancyAdapter(vacancyList)
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filtersFragment)
        }
        setupRecyclerView()
        observeViewModel()
        startRequest("Стоматолог")
    }

    private fun observeViewModel() {
        viewModel.searchVacancyResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchVacancyResult.Error -> updateUI(SearchUIState.CONNECTION_ERROR)
                SearchVacancyResult.EmptyResult -> updateUI(SearchUIState.EMPTY_SEARCH)
                SearchVacancyResult.NoInternet -> updateUI(SearchUIState.NO_INTERNET)
                is SearchVacancyResult.Success -> showVacancy(state.response.items)
            }
        }
    }

    private fun updateUI(searchUIState: SearchUIState) {
        when (searchUIState) {
            SearchUIState.CONNECTION_ERROR -> {
                TODO()
            }

            SearchUIState.EMPTY_SEARCH -> {
                TODO()
            }

            SearchUIState.LOADING -> {
                TODO()
            }

            SearchUIState.NO_INTERNET -> TODO()
        }
    }

    private fun showVacancy(items: List<VacancyItem>) {
        binding.recyclerView.visibility = View.VISIBLE
        vacancyList.addAll(items)
        vacancyAdapter.notifyDataSetChanged()
    }

    private fun startRequest(inputText: String) {
        val queryParams = mapOf("text" to "{$inputText}", "per_page" to "50")
        viewModel.searchVacancies(queryParams)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = vacancyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}




