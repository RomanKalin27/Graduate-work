package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

class FiltersFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPlaceWork()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.jobEditText.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_choosePlaceWorkFragment)
        }
        binding.industryEditText.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_chooseIndustry)
        }
    }

    private fun setPlaceWork() {
        setFragmentResultListener("key_choose")
        { _, bundle ->
            binding.jobEditText.setText(bundle.getString("place_work").orEmpty())
        }
    }
}