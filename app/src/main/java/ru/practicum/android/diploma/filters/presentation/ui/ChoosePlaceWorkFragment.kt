package ru.practicum.android.diploma.filters.presentation.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectLocationBinding
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.BUNDLE_KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.KEY
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class ChoosePlaceWorkFragment : Fragment() {
    private lateinit var binding: FragmentSelectLocationBinding
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
    }

    private fun setFilters() {
        requireActivity().supportFragmentManager.setFragmentResultListener(KEY, this)
        { key, bundle ->
            val country = bundle.getString(BUNDLE_KEY).orEmpty()
            if (!country.isNullOrEmpty()) {
                binding.countryEditText.setText(country)
            }
            setClearBtn()
        }
    }

    private fun setClearBtn() {
        if (!binding.countryEditText.text.isNullOrEmpty()) {
            binding.countryTextField.setPadding(
                0, requireContext().resources.getDimension(R.dimen.margin_14).toInt(),
                requireContext().resources.getDimension(R.dimen.margin_24).toInt(), 0
            )
            binding.countryTextField.setHintTextAppearance(R.style.Text_Regular_12_400)
            binding.countryClearBtn.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
            binding.countryClearBtn.setOnClickListener {
                binding.countryEditText.text?.clear()
                setClearBtn()
            }
        } else {
            binding.countryTextField.setPadding(
                0, requireContext().resources.getDimension(R.dimen.margin_4).toInt(),
                requireContext().resources.getDimension(R.dimen.margin_24).toInt(), 0
            )
            binding.countryTextField.setHintTextAppearance(R.style.Text_Regular_16_400)
            binding.countryClearBtn.setImageDrawable(requireContext().getDrawable(R.drawable.arrow_forward))
            binding.countryClearBtn.setOnClickListener {
                findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseCountryFragment)
            }
        }
    }


        /* val country = requireArguments().getString(ARGS_COUNTRY).orEmpty()
        val region = requireArguments().getString(ARGS_REGION).orEmpty()
        if (!country.isNullOrEmpty()) {
            binding.countryEditText.setText(country)
        }
        if (!region.isNullOrEmpty()){
            binding.regionEditText.setText(region)
        }*/
    companion object {
        private const val ARGS_COUNTRY = "args_country"
        private const val ARGS_REGION = "args_region"
        fun createArgs(country: Area, region: Area): Bundle =
            bundleOf(ARGS_COUNTRY to country.name, ARGS_REGION to region.name)
    }
}