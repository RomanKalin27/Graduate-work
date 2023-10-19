package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.ChangeTextFieldUtil
import ru.practicum.android.diploma.databinding.FragmentSelectLocationBinding
import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.filters.domain.models.ChooseResult
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.BUNDLE_KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.FILTER_COUNTRY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment.Companion.KEY_R
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment.Companion.REGION_KEY
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class ChoosePlaceWorkFragment : Fragment() {
    private lateinit var binding: FragmentSelectLocationBinding
    private var country: AreasDTO = AreasDTO.emptyArea
    private var region: Area = Area.emptyArea
    private val viewModel by viewModel<FiltersViewModel>()
    private var areasList = ArrayList<AreasDTO>()
    private var areasRegionList = ArrayList<AreasDTO>()
    private var area = ArrayList<AreasDTO>()
    private var countryArea = AreasDTO.emptyArea
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getCountry()
        getRegions()
        observeViewModel()
        setFilters()
        setSavedFilters()
        showChooseBtn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRegions()
        getCountry()
        initListeners()
    }
    private fun setSavedFilters(){
        setFragmentResultListener(COUNTRY_AND_REGION){_,bundle ->
            val country = (bundle.getString(COUNTRY_JSON_KEY)?.let {
                Json.decodeFromString<AreasDTO>(it)
            }?: AreasDTO.emptyArea)
            if (country != AreasDTO.emptyArea) {
                binding.countryEditText.setText(country.name)
            }
            val region = (bundle.getString(REGION_JSON_KEY)?.let {
                Json.decodeFromString<Area>(it)
            }?: Area.emptyArea)
            if (region != Area.emptyArea) {
                binding.regionEditText.setText(region.name)
            }
            setFragmentResult(
                ChooseCountryFragment.FILTER_KEY,
                bundleOf(FILTER_COUNTRY to Json.encodeToString(country))
            )
            changeCountryField()
            changeRegionField()
        }
    }

    private fun setFilters() {
        setFragmentResultListener(KEY)
        { _, bundle ->
            country = (bundle.getString(BUNDLE_KEY)?.let { Json.decodeFromString<AreasDTO>(it) }
                ?: AreasDTO.emptyArea)
            if (country != AreasDTO.emptyArea) {
                binding.countryEditText.setText(country.name)
            }
        }
        setFragmentResultListener(KEY_R)
        { _, bundle ->
            region = (bundle.getString(REGION_KEY)?.let { Json.decodeFromString<Area>(it) }
                ?: Area.emptyArea)
            if (region != Area.emptyArea) {
                binding.regionEditText.setText(region.name)
            }
        }
        if (region != Area.emptyArea) {
            area.clear()
            if (region.parentId?.length!! <= 3) {
                areasList.removeIf { it.id != region.parentId }
                area.addAll(areasList)
            } else {
                val a = areasRegionList.flatMap { it.areas }.filter { it.id == region.parentId }
                areasList.removeIf { it.id != a[0].parentId }
                area.addAll(areasList)
            }
            countryArea = area[0]
            binding.countryEditText.setText(area[0].name)
            setFragmentResult(
                ChooseCountryFragment.FILTER_KEY,
                bundleOf(FILTER_COUNTRY to Json.encodeToString(area[0]))
            )
            country = AreasDTO(id = area[0].id, name = area[0].name, emptyList())
        }
        changeCountryField()
        changeRegionField()
    }

    private fun getCountry() {
        viewModel.getCountry()
    }

    private fun getRegions() {
        viewModel.getRegions()
    }

    private fun changeCountryField() {
        ChangeTextFieldUtil.changeTextField(
            binding.countryEditText,
            binding.countryTextField,
            binding.countryClearBtn,
            requireContext(),
        )
    }

    private fun changeRegionField() {
        ChangeTextFieldUtil.changeTextField(
            binding.regionEditText,
            binding.regionTextField,
            binding.regionClearBtn,
            requireContext(),
        )
    }

    private fun observeViewModel() {
        viewModel.chooseResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                ChooseResult.EmptyResult -> {}
                is ChooseResult.Error -> {}
                ChooseResult.NoInternet -> {}
                is ChooseResult.Success -> areasList.addAll(state.response)
            }
        }
        viewModel.chooseRegionResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                ChooseRegionsResult.EmptyResult -> {}
                is ChooseRegionsResult.Error -> {}
                ChooseRegionsResult.NoInternet -> {}
                is ChooseRegionsResult.Success -> areasRegionList.addAll(state.response)
            }
        }
    }

    private fun chooseFilters() {
        var countryJson: String? = null
        var regionJson: String? = null
        if (binding.countryEditText.text.toString().isNotEmpty()) {
            countryJson = Json.encodeToString(countryArea)
        }
        if (binding.regionEditText.text.toString().isNotEmpty()) {
            regionJson = Json.encodeToString(region)
        }
        setFragmentResult(
            KEY_CHOOSE,
            bundleOf(COUNTRY_JSON_KEY to countryJson, REGION_JSON_KEY to regionJson)
        )
        findNavController().navigateUp()
    }

    private fun showChooseBtn() {
        binding.regionEditText.setText(region.name)
        binding.btnChoose.isVisible = !binding.countryEditText.text.isNullOrEmpty()
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCountry.setOnClickListener {
            findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseCountryFragment)
        }
        binding.countryClearBtn.setOnClickListener {
            binding.countryEditText.text?.clear()
            setFragmentResult(
                ChooseCountryFragment.FILTER_KEY,
                bundleOf(ChooseCountryFragment.FILTER_COUNTRY to Json.encodeToString(AreasDTO.emptyArea))
            )
            changeCountryField()
            showChooseBtn()
        }
        binding.btnRegion.setOnClickListener {
            findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseRegionFragment)
        }
        binding.regionClearBtn.setOnClickListener {
            region = Area.emptyArea
            showChooseBtn()
            changeRegionField()
        }
        binding.btnChoose.setOnClickListener {
            chooseFilters()
        }
    }

    companion object {
        const val KEY_CHOOSE = "key_choose"
        const val COUNTRY_JSON_KEY = "COUNTRY_JSON_KEY"
        const val REGION_JSON_KEY = "REGION_JSON_KEY"
        const val COUNTRY_AND_REGION = "COUNTRY_AND_REGION"
    }
}