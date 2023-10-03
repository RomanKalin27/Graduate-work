package ru.practicum.android.diploma.search.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel

class SearchFragment : Fragment() {
    private val viewModel by viewModel<SearchViewModel>()
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
        // Для отправки запроса нужно передать в queryParams текст вместо Android
        // per_page - количество результатов
        val queryParams = mapOf("text" to "Android", "per_page" to "20")

        // Отправка запроса
        // viewModel.searchVacancies(queryParams)

        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is SearchResult.Success -> {
                    val response = result.response
                    // Вывод в консоль имя первой вакансии из сервера
                    println(response.items[0].name)
                }

                is SearchResult.Error -> {
                    val exception = result.exception
                    binding.searchPlaceholder.visibility = View.VISIBLE
                    binding.chip.visibility = View.VISIBLE

                }
            }
        }
    }
}