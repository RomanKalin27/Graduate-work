package ru.practicum.android.diploma.filters.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.models.AreaDomain
import ru.practicum.android.diploma.filters.domain.models.Areas
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.filters.domain.models.ChooseResult
import ru.practicum.android.diploma.filters.domain.models.FiltersState

class FiltersViewModel(
    private val filterInteractor: FilterInteractor,
    private val chooseCountryInteractor: ChooseCountryInteractor,
    private val chooseRegionInteractor: ChooseRegionInteractor,
    private val chooseIndustryInteractor: ChooseIndustryInteractor,
) : ViewModel() {
    private val _stateLiveData = MutableLiveData<FiltersState>()
    private var emptyFilters = FiltersState(null, null, null, null, false)
    fun observeState(): LiveData<FiltersState> = _stateLiveData

    private val _chooseResult: MutableLiveData<ChooseResult> = MutableLiveData()
    val chooseResult: LiveData<ChooseResult> = _chooseResult
    private val _chooseRegionResult: MutableLiveData<ChooseRegionsResult> = MutableLiveData()
    val chooseRegionResult: LiveData<ChooseRegionsResult> = _chooseRegionResult
    private val _chooseIndustryResult: MutableLiveData<ChooseIndustryResult> = MutableLiveData()
    val chooseIndustryResult: LiveData<ChooseIndustryResult> = _chooseIndustryResult
    private val _convert: MutableLiveData<List<AreaDomain>> = MutableLiveData()
    val convert: LiveData<List<AreaDomain>> = _convert

    init {
        loadFilters()
    }

    fun getCountry() {
        viewModelScope.launch {
            chooseCountryInteractor.execute().collect { result ->
                _chooseResult.postValue(result)
            }
        }
    }

    fun convert(areas: List<Areas>, country: Areas) {
        if (country != Areas.emptyArea) {
            val result = filterInteractor.filterConvertCountry(areas, country)
            _convert.postValue(result)
        } else {
            _convert.postValue(filterInteractor.filterConvertRegions(areas))
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            chooseRegionInteractor.getAreas().collect { result ->
                _chooseRegionResult.postValue(result)
            }
        }
    }

    fun getIndustry() {
        viewModelScope.launch {
            chooseIndustryInteractor.getIndustry().collect { result ->
                _chooseIndustryResult.postValue(result)
            }
        }
    }

    private fun loadFilters() {
        emptyFilters.country = filterInteractor.getCountry()
        emptyFilters.region = filterInteractor.getRegion()
        emptyFilters.industry = filterInteractor.getIndustry()
        emptyFilters.lowestSalary = filterInteractor.getExpectedSalary()
        emptyFilters.removeNoSalary = filterInteractor.getRemoveNoSalary()
        _stateLiveData.postValue(emptyFilters)
    }

    fun saveFilters(
        expectedSalary: String?,
        removeNoSalary: Boolean,
    ) {
        filterInteractor.saveFilters(
            emptyFilters.country,
            emptyFilters.region,
            emptyFilters.industry,
            expectedSalary,
            removeNoSalary,
        )
    }

    fun removeFilters() {
        filterInteractor.removeFilters()
        loadFilters()
    }

    fun getLocation(countryJson: String?, regionJson: String?) {
        emptyFilters.country = countryJson
        emptyFilters.region = regionJson
        _stateLiveData.postValue(emptyFilters)
    }

    fun getIndustry(industryJson: String?) {
        emptyFilters.industry = industryJson
        _stateLiveData.postValue(emptyFilters)
    }
}