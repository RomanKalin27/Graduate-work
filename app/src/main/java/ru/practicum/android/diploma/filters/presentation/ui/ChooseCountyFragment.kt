package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.filters.presentation.rv.CountryAdapter
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class ChooseCountryFragment : Fragment() {
    companion object {
        const val KEY = "KEY"
        const val BUNDLE_KEY = "BUNDLE_KEY"
    }

    private lateinit var binding: FragmentSelectCountryBinding
    private lateinit var countryAdapter: CountryAdapter
    private var countryList = ArrayList<Area>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSelectCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryList.add(Area("Россия"))
        initCountryAdapter()
        binding.btnBack.setOnClickListener {
            setResult(null)
        }
    }

    private fun initCountryAdapter(): CountryAdapter {
        countryAdapter = CountryAdapter {
            setResult(it.name)
        }
        countryAdapter.countryList = countryList
        binding.regionRecycler.adapter = countryAdapter
        return countryAdapter
    }

    private fun setResult(countryName: String?) {
        requireActivity().supportFragmentManager.setFragmentResult(
            KEY,
            bundleOf(BUNDLE_KEY to countryName)
        )
        findNavController().navigateUp()
    }
}