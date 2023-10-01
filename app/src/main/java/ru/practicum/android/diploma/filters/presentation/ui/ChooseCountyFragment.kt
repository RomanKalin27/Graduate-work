package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.filters.presentation.rv.CountryAdapter
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class ChooseCountryFragment : Fragment() {
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
            findNavController().navigateUp()
        }
    }

    private fun initCountryAdapter(): CountryAdapter {
        countryAdapter = CountryAdapter {
            findNavController().navigate(
                R.id.action_chooseCountryFragment_to_choosePlaceWorkFragment,
                ChoosePlaceWorkFragment.createArgs(it,Area.emptyArea)
            )
        }
        countryAdapter.countryList = countryList
        binding.regionRecycler.adapter = countryAdapter
        return countryAdapter
    }
}