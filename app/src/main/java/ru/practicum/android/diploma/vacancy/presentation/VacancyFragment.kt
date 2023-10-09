package ru.practicum.android.diploma.vacancy.presentation

import android.content.Intent
import android.net.Uri
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.icSharing.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.putExtra(Intent.EXTRA_TEXT, "ССЫЛКА")
            share.type = "text/plane"
            requireContext().startActivity(share)
        }
        binding.vacancyContactEmailValue.setOnClickListener {
            requireContext().startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:kalinroman034@gmail.com")))
        }
        binding.vacancyContactPhoneValue.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "8-800-555-35-35", null))
            requireContext().startActivity(call)
        }
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