package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentRegionsBinding
import ru.practicum.android.diploma.filters.presentation.rv.RegionAdapter
import ru.practicum.android.diploma.search.data.dto.response_models.Area


class ChooseRegionFragment : Fragment() {
    private lateinit var binding: FragmentRegionsBinding
    private lateinit var regionAdapter: RegionAdapter
    private var regionList = ArrayList<Area>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRegionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCountryAdapter()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initCountryAdapter(): RegionAdapter {
        regionList.add(Area("Москва"))
        regionAdapter = RegionAdapter {
            setResult(it.name)
        }
        regionAdapter.regionList = regionList
        binding.regionRecycler.adapter = regionAdapter
        return regionAdapter
    }

    private fun setResult(regionName: String?) {
        setFragmentResult(
            KEY_R,
            bundleOf(REGION_KEY to regionName)
        )
    }

    companion object {
        const val KEY_R = "KEY_R"
        const val REGION_KEY = "REGION_KEY"
    }
}