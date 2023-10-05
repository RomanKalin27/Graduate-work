package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.KEY_CHOOSE
import ru.practicum.android.diploma.filters.presentation.ui.ChoosePlaceWorkFragment.Companion.PLACE_WORK
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel

class FiltersFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private val vm by viewModel<FiltersViewModel>()

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
        binding.jobEditText.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_choosePlaceWorkFragment)
        }
        binding.industryEditText.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_chooseIndustry)
        }
        binding.salaryEditText.addTextChangedListener {
            showRemoveBtn()
        }
        binding.noSalaryLayout.setOnClickListener {
            binding.noSalaryCheckbox.isChecked = !binding.noSalaryCheckbox.isChecked
            showRemoveBtn()
        }
        binding.btnChoose.setOnClickListener {
            vm.saveFilters(
                binding.jobEditText.text.toString(),
                binding.industryEditText.text.toString(),
                binding.salaryEditText.text.toString(),
                binding.noSalaryCheckbox.isChecked
            )
        }

        binding.btnRemove.setOnClickListener {
            vm.removeFilters()
            binding.jobEditText.text.clear()
        }

        vm.observeState().observe(viewLifecycleOwner) {
            binding.jobEditText.setText(it.location)
            binding.industryEditText.setText(it.industry)
            binding.salaryEditText.setText(it.lowestSalary)
            binding.noSalaryCheckbox.isChecked = it.removeNoSalary
        }
    }

    private fun showRemoveBtn() {
        if (binding.jobEditText.text.isNotEmpty() || binding.industryEditText.text.isNotEmpty()
            || binding.salaryEditText.text.isNotEmpty() || binding.noSalaryCheckbox.isChecked
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
            if (!bundle.getString(PLACE_WORK).isNullOrEmpty()) {
                vm.getLocation(bundle.getString(PLACE_WORK))
            }
        }
    }
}