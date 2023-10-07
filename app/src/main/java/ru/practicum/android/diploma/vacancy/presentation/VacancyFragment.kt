package ru.practicum.android.diploma.vacancy.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.data.dto.response_models.VacancyItem

class VacancyFragment : Fragment() {
    private val viewModel by viewModel<VacancyViewModel>()
    private val currentVacancy by lazy { retrieveVacancy() }
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun retrieveVacancy(): VacancyItem {
        val jsonVacancy = requireArguments().getString(KEY_VACANCY)
        return Gson().fromJson(jsonVacancy, VacancyItem::class.java)
    }

    companion object {
        const val KEY_VACANCY = "vacancy"

        fun createArgs(vacancy: VacancyItem): Bundle {
            val gson = Gson()
            val jsonVacancy = gson.toJson(vacancy)
            val bundle = Bundle()
            bundle.putString(KEY_VACANCY, jsonVacancy)
            return bundle
        }
    }
}