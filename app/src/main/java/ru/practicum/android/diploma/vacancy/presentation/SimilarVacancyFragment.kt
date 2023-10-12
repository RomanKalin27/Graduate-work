package ru.practicum.android.diploma.vacancy.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.common.utils.Constants.CLICK_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.SearchState
import ru.practicum.android.diploma.search.presentation.rv.VacancyAdapter


class SimilarVacancyFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {

    private val viewModel by viewModel<SimilarVacancyViewModel>()
    private lateinit var adapter: VacancyAdapter
    private lateinit var onVacancyClickDebounce: (Vacancy) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSimilarVacanciesBinding {
        return FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()

        viewModel.getSimilarVacanciesById(requireArguments().getString(VACANCY_ID)!!)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.FirstLoading -> showLoading()
                SearchState.AddLoading -> {}
                SearchState.StopLoad -> {}
                is SearchState.VacancyContent -> showVacanciesList(state.vacancies)
                is SearchState.Error -> showError(state.errorMessage)
                is SearchState.Empty -> showEmpty(state.message)
            }
        }

        initClickListeners()
    }

    private fun initAdapters() {
        adapter = VacancyAdapter(ArrayList())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(){
        binding.recyclerView.visibility = View.GONE
        binding.progressBarForLoad.visibility = View.VISIBLE
    }

    private fun showVacanciesList(vacancies: List<Vacancy>) {
        binding.progressBarForLoad.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        //
        adapter.setVacancies(vacancies)

        adapter.notifyDataSetChanged()
    }

    private fun showError(errorMessage: String) {
        showToast(errorMessage)
        binding.recyclerView.visibility = View.GONE
        binding.progressBarForLoad.visibility = View.GONE
        }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun showEmpty(emptyMessage: String) {

        binding.recyclerView.visibility = View.GONE
        binding.progressBarForLoad.visibility = View.GONE
    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter.itemClickListener = { position, vacancy ->
            onVacancyClickDebounce(vacancy)
        }

        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { vacancy ->
            openVacancy(vacancy)
        }
    }

    private fun openVacancy(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_similarVacancyFragment_to_vacancyFragment,
            VacancyFragment.createArgs(Gson().toJson(vacancy))
        )
    }

    companion object {
        const val VACANCY_ID = "vacancy_id"

        fun createArgs(vacancyId: String): Bundle = bundleOf(VACANCY_ID to vacancyId)
    }
}