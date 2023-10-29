package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentRegionsBinding
import ru.practicum.android.diploma.filters.domain.models.AreaDomain
import ru.practicum.android.diploma.filters.domain.models.Areas
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.filters.presentation.rv.RegionAdapter
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.FILTER_COUNTRY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.FILTER_KEY
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel


class ChooseRegionFragment : Fragment() {
    private lateinit var binding: FragmentRegionsBinding
    private lateinit var regionAdapter: RegionAdapter
    private lateinit var textWatcher: TextWatcher
    private var debounce: ((String) -> Unit)? = null
    private val viewModel by viewModel<FiltersViewModel>()
    private var regionList = ArrayList<AreaDomain>()
    private var regionListSaved = ArrayList<AreaDomain>()
    private var regionListFilter = ArrayList<AreaDomain>()
    private var country: Areas = Areas.emptyArea
    private var hasInternet: Boolean = false
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
        debounce()
        initCountryAdapter()
        getRegions()
        observeViewModel()
        initListeners()
    }

    private fun observeViewModel() {
        viewModel.chooseRegionResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                ChooseRegionsResult.EmptyResult -> {
                    showErrorList()
                }

                is ChooseRegionsResult.Error -> {
                    showErrorList()
                    println(state.exception)
                }

                ChooseRegionsResult.NoInternet -> {
                    showErrorList()
                }

                is ChooseRegionsResult.Success -> {
                    showRegions(state.response)
                }
            }
        }
        viewModel.convert.observe(viewLifecycleOwner) {
            regionList.clear()
            regionList.addAll(it)
            regionAdapter.notifyDataSetChanged()
            binding.regionRecycler.visibility = View.VISIBLE
            regionListSaved.addAll(it)
            binding.noInternetImage.visibility = View.GONE
            binding.noInternetText.visibility = View.GONE
        }
    }

    private fun showRegions(regions: List<Areas>) {
        hasInternet = true
        viewModel.convert(regions, setFilters())
    }

    private fun getRegions() {
        viewModel.getRegions()
    }

    private fun showErrorList() {
        hasInternet = false
        binding.noInternetImage.visibility = View.VISIBLE
        binding.noInternetText.visibility = View.VISIBLE
        binding.regionRecycler.visibility = View.GONE
    }

    private fun initCountryAdapter(): RegionAdapter {
        regionAdapter = RegionAdapter {
            setResult(it)
        }
        regionAdapter.regionList = regionList
        binding.regionRecycler.adapter = regionAdapter
        return regionAdapter
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (hasInternet) {
                    debounceSearch(p0.toString().trim())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        binding.regionEditText.addTextChangedListener(textWatcher)
    }

    private fun showErrorNothing() {
        binding.nothingSearchImage.visibility = View.VISIBLE
        binding.nothingSearchText.visibility = View.VISIBLE
    }

    private fun hideErrorNothing() {
        binding.nothingSearchImage.visibility = View.GONE
        binding.nothingSearchText.visibility = View.GONE
    }

    private fun debounceSearch(p0: String) {
        debounce?.let { it(p0) }
    }

    private fun debounce() {
        debounce = debounce<String>(
            SEARCH_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true,
            action = { text ->
                if (text.isNullOrEmpty()) {
                    hideErrorNothing()
                    regionList.clear()
                    regionListFilter.clear()
                    regionList.addAll(regionListSaved)
                    regionAdapter.notifyDataSetChanged()
                } else {
                    regionList.clear()
                    regionListSaved.forEach {
                        if (it.name!!.lowercase() == text.lowercase()) {
                            regionListFilter.add(it)
                        }
                    }
                    if (regionListFilter.isEmpty()) {
                        showErrorNothing()
                    } else {
                        hideErrorNothing()
                    }
                    regionList.addAll(regionListFilter)
                    regionAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun setResult(region: AreaDomain) {
        setFragmentResult(
            KEY_R,
            bundleOf(REGION_KEY to Json.encodeToString(region))
        )
        findNavController().navigateUp()
    }

    private fun setFilters(): Areas {
        setFragmentResultListener(FILTER_KEY)
        { _, bundle ->
            country = (bundle.getString(FILTER_COUNTRY)?.let { Json.decodeFromString<Areas>(it) }
                ?: Areas.emptyArea)
        }
        return country
    }

    companion object {
        const val KEY_R = "KEY_R"
        const val REGION_KEY = "REGION_KEY"
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }
}