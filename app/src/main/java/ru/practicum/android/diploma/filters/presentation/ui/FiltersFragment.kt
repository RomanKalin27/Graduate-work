package ru.practicum.android.diploma.filters.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.ChangeTextFieldUtil
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.domain.models.AreaDomain
import ru.practicum.android.diploma.filters.domain.models.Areas
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.COUNTRY_AND_REGION
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.COUNTRY_JSON_KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.KEY_CHOOSE
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.REGION_JSON_KEY
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel

class FiltersFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private var isChecked = false
    private var country: Areas = Areas.emptyArea
    private var region: AreaDomain = AreaDomain.emptyArea
    private var industry: Industry = Industry.emptyIndustry
    private val vm by viewModel<FiltersViewModel>()

    companion object {
        const val SET_FILTERS_KEY = "SET_FILTERS_KEY"
        const val KEY_INDUSTRY = "KEY_INDUSTRY"
        const val INDUSTRY = "INDUSTRY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setPlaceWork()
        showRemoveBtn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLocation.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_choosePlaceWorkFragment)
        }
        binding.locationClearBtn.setOnClickListener {
            binding.locationEditText.text?.clear()
            vm.getLocation(null, null)
            changeLocationField()
        }
        binding.btnIndustry.setOnClickListener {
            val currentIndustry = binding.industryEditText.text.toString()
            val bundleIndustry = bundleOf("INDUSTRY_TEXT" to currentIndustry)
            findNavController().navigate(
                R.id.action_filtersFragment_to_chooseIndustry,
                bundleIndustry
            )
        }
        binding.industryClearBtn.setOnClickListener {
            binding.industryEditText.text?.clear()
            vm.getIndustry(null)
            changeIndustryField()
        }
        binding.salaryEditText.addTextChangedListener {
            changeSalaryField()
        }
        binding.salaryEditText.setOnFocusChangeListener { _, _ ->
            changeSalaryField()
        }
        binding.salaryEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.salaryEditText.isFocusable = false
                binding.salaryEditText.isFocusableInTouchMode = true
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }
        binding.salaryClearBtn.setOnClickListener {
            binding.salaryEditText.text.clear()
        }
        binding.noSalaryLayout.setOnClickListener {
            isChecked = !isChecked
            changeCheckBox()
        }
        binding.btnChoose.setOnClickListener {
            vm.saveFilters(
                binding.salaryEditText.text.toString(),
                isChecked,
            )
            setFragmentResult(
                SET_FILTERS_KEY,
                bundleOf()
            )
            findNavController().navigateUp()
        }

        binding.btnRemove.setOnClickListener {
            vm.removeFilters()
            binding.locationEditText.text?.clear()
            binding.industryEditText.text?.clear()
        }

        vm.observeState().observe(viewLifecycleOwner) {
            setFragmentResult(
                COUNTRY_AND_REGION,
                bundleOf(
                    COUNTRY_JSON_KEY to it.country,
                    REGION_JSON_KEY to it.region
                )
            )
            if (!it.country.isNullOrEmpty()) {
                country = Json.decodeFromString(it.country ?: "")
                if (!it.region.isNullOrEmpty()) {
                    region = Json.decodeFromString(it.region ?: "")
                    val location = country.name + "," + region.name
                    binding.locationEditText.setText(location)
                } else {
                    region = AreaDomain.emptyArea
                    binding.locationEditText.setText(country.name)
                }
            } else {
                country = Areas.emptyArea
                region = AreaDomain.emptyArea
            }
            if (!it.industry.isNullOrEmpty()) {
                industry = Json.decodeFromString(it.industry ?: "")
                binding.industryEditText.setText(industry.name)
            } else {
                industry = Industry.emptyIndustry
            }
            binding.salaryEditText.setText(it.lowestSalary)
            isChecked = it.removeNoSalary
            changeCheckBox()
            changeLocationField()
            changeIndustryField()
        }
    }

    private fun changeLocationField() {
        ChangeTextFieldUtil.changeTextField(
            binding.locationEditText,
            binding.locationTextField,
            binding.locationClearBtn,
            requireContext(),
        )
        showRemoveBtn()
    }

    private fun changeIndustryField() {
        ChangeTextFieldUtil.changeTextField(
            binding.industryEditText,
            binding.industryTextField,
            binding.industryClearBtn,
            requireContext(),
        )
        showRemoveBtn()
    }

    private fun changeSalaryField(){
       // binding.salaryEditText.isCursorVisible = true
        if (binding.salaryEditText.text.isEmpty()) {
            binding.textExpectedSalary.setTextColor(requireContext().getColor(R.color.hint))
            binding.salaryClearBtn.isVisible = false
        } else {
            if(!binding.salaryEditText.isFocused){
                binding.textExpectedSalary.setTextColor(requireContext().getColor(R.color.yp_black))
                binding.salaryClearBtn.isVisible = false
            } else {
                binding.textExpectedSalary.setTextColor(requireContext().getColor(R.color.blue))
                binding.salaryClearBtn.isVisible = true
            }
        }
        showRemoveBtn()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeCheckBox() {
        if (isChecked) {
            binding.noSalaryCheckbox.background =
                requireContext().getDrawable(R.drawable.pressed_check_box)
        } else {
            binding.noSalaryCheckbox.background =
                requireContext().getDrawable(R.drawable.empty_check_box)
        }
        showRemoveBtn()
    }

    private fun showRemoveBtn() {
        if (binding.locationEditText.text.toString()
                .isNotEmpty() || binding.industryEditText.text.toString().isNotEmpty()
            || binding.salaryEditText.text.isNotEmpty() || isChecked
        ) {
            binding.btnRemove.isVisible = true
            binding.btnChoose.text = requireContext().getString(R.string.apply)
        } else {
            binding.btnRemove.isVisible = false
            binding.btnChoose.text = requireContext().getString(R.string.select)
        }
    }

    private fun setPlaceWork() {
        setFragmentResultListener(KEY_CHOOSE)
        { _, bundle ->
            if (!bundle.getString(COUNTRY_JSON_KEY).isNullOrEmpty()) {
                vm.getLocation(
                    bundle.getString(COUNTRY_JSON_KEY),
                    bundle.getString(REGION_JSON_KEY)
                )
            }
        }
        setFragmentResultListener(KEY_INDUSTRY) { _, bundle ->
            vm.getIndustry(bundle.getString(INDUSTRY))
        }
    }

}