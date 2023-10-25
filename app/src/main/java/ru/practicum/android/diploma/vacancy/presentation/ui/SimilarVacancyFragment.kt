package ru.practicum.android.diploma.vacancy.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.common.utils.Constants
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.core.root.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.presentation.models.SearchUIState
import ru.practicum.android.diploma.search.presentation.rv.VacancyAdapter
import ru.practicum.android.diploma.vacancy.presentation.view_model.SimilarVacanciesViewModel

class SimilarVacancyFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {
    private val viewModel by viewModel<SimilarVacanciesViewModel>()
    private val similarVacancyList = ArrayList<Vacancy>()
    private lateinit var onVacancyClickDebounce: (Vacancy) -> Unit
    private val vacancyAdapter = VacancyAdapter(similarVacancyList)
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSimilarVacanciesBinding {
        return FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        onVacancyClickDebounce = debounce(
            Constants.CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { item ->
            navigateToVacancyDetail(item.id)
        }
        viewModel.searchVacancies(requireArguments().getString(KEY_VACANCY) ?: "")
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        vacancyAdapter.itemClickListener = { position, vacancy ->
            (activity as? RootActivity)?.animateBottomNavigationView()
            onVacancyClickDebounce(vacancy)
        }
        with(binding.recyclerView) {
            adapter = vacancyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.similarVacanciesResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchVacancyResult.Error -> updateUI(SearchUIState.CONNECTION_ERROR)
                SearchVacancyResult.EmptyResult -> updateUI(SearchUIState.EMPTY_SEARCH)
                SearchVacancyResult.NoInternet -> updateUI(SearchUIState.NO_INTERNET)
                is SearchVacancyResult.Success -> showVacancy(state.response.vacancies)
                SearchVacancyResult.Loading -> updateUI(SearchUIState.LOADING)
                else -> {}
            }
        }
    }

    private fun updateUI(searchUIState: SearchUIState) {
        with(binding) {
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            recyclerView.isVisible = false
            similarVacancyList.clear()

            when (searchUIState) {
                SearchUIState.CONNECTION_ERROR -> placeholderServerError.isVisible = true
                SearchUIState.EMPTY_SEARCH -> placeholderNoVacancies.isVisible = true
                SearchUIState.NO_INTERNET -> noInternetPlaceholder.isVisible = true
                SearchUIState.LOADING -> progressBar.isVisible = true
            }

            vacancyAdapter.notifyDataSetChanged()
        }
    }

    private fun showVacancy(items: List<Vacancy>) {
        with(binding) {
            recyclerView.isVisible = true
            placeholderServerError.isVisible = false
            placeholderNoVacancies.isVisible = false
            noInternetPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
        similarVacancyList.clear()
        similarVacancyList.addAll(items)
        vacancyAdapter.notifyDataSetChanged()
    }

    private fun navigateToVacancyDetail(item: String) {
        setFragmentResult(
            SIMILAR_VACANCY_KEY,
            bundleOf(SIMILAR_VACANCY to item)
        )
        findNavController().navigate(R.id.action_similarVacancyFragment_to_vacancyFragment, VacancyFragment.createArgs(item))
    }

    companion object {
        const val KEY_VACANCY = "vacancy"
        const val SIMILAR_VACANCY_KEY = "SIMILAR_VACANCY_KEY"
        const val SIMILAR_VACANCY = "SIMILAR_VACANCY"
        fun createArgs(vacancy: String): Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_VACANCY, vacancy)
            return bundle
        }
    }
}