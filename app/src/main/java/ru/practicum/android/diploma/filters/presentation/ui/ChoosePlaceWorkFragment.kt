package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectLocationBinding
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.BUNDLE_KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment.Companion.KEY_R
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment.Companion.REGION_KEY

class ChoosePlaceWorkFragment : Fragment() {
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var country: String
    private lateinit var region: String
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
        setFilters()
        showChooseBtn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCountry.setOnClickListener {
            findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseCountryFragment)
        }
        binding.regionEditText.setOnClickListener {
            findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseRegionFragment)
        }
        binding.btnChoose.setOnClickListener {
            chooseFilters()
        }
    }

    private fun setFilters() {
        setFragmentResultListener(KEY)
        { _, bundle ->
            country = bundle.getString(BUNDLE_KEY).orEmpty()
            if (country.isNotEmpty()) {
                binding.countryEditText.setText(country)
            }
        }
        setFragmentResultListener(KEY_R)
        { _, bundle ->
            region = bundle.getString(REGION_KEY).orEmpty()
            if (region.isNotEmpty()) {
                binding.regionEditText.setText(region)
            }
        }
        changeTextField()
    }


    private fun changeTextField() {
        if (!binding.countryEditText.text.isNullOrEmpty()) {
            binding.countryTextField.setPadding(
                0, requireContext().resources.getDimension(R.dimen.margin_14).toInt(),
                requireContext().resources.getDimension(R.dimen.margin_24).toInt(), 0
            )
            binding.countryTextField.isEnabled = true
            binding.countryTextField.setHintTextAppearance(R.style.Text_Regular_12_400)
            binding.countryClearBtn.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
            binding.countryClearBtn.setOnClickListener {
                binding.countryEditText.text?.clear()
                changeTextField()
            }
        } else {
            binding.countryTextField.setPadding(
                0, requireContext().resources.getDimension(R.dimen.margin_4).toInt(),
                requireContext().resources.getDimension(R.dimen.margin_24).toInt(), 0
            )
            binding.countryTextField.isEnabled = false
            binding.countryTextField.setHintTextAppearance(R.style.Text_Regular_16_400)
            binding.countryClearBtn.setImageDrawable(requireContext().getDrawable(R.drawable.arrow_forward))
            binding.countryClearBtn.setOnClickListener {
                findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseCountryFragment)
            }
        }
    }

    private fun chooseFilters() {
        /*  if (country.isNullOrEmpty()) {
              binding.btnChoose.visibility = View.GONE
          } else {
              binding.btnChoose.visibility = View.VISIBLE*/
        if (region.isNullOrEmpty()) {
            setFragmentResult(
                KEY_CHOOSE,
                bundleOf(PLACE_WORK to country)
            )
        } else {
            val result = "$country,$region"
            setFragmentResult(
                KEY_CHOOSE,
                bundleOf(PLACE_WORK to result)
            )
        }
        findNavController().navigateUp()
    }

    private fun showChooseBtn() {
        region = binding.regionEditText.text.toString()
        binding.btnChoose.isVisible = !binding.countryEditText.text.isNullOrEmpty()
    }

    companion object {
        const val KEY_CHOOSE = "key_choose"
        const val PLACE_WORK = "place_work"
    }
}