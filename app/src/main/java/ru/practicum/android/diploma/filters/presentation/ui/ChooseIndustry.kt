package ru.practicum.android.diploma.filters.presentation.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.Constants.SEARCH_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.filters.presentation.rv.IndustryAdapter
import ru.practicum.android.diploma.filters.presentation.ui.FiltersFragment.Companion.INDUSTRY
import ru.practicum.android.diploma.filters.presentation.ui.FiltersFragment.Companion.KEY_INDUSTRY
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel

class ChooseIndustry : Fragment() {
    private lateinit var binding: FragmentIndustryBinding
    private lateinit var industryAdapter: IndustryAdapter
    private var debounce: ((String) -> Unit)? = null
    private var industryList = ArrayList<Industry>()
    private var industryListFilter = ArrayList<Industry>()
    private lateinit var textWatcher: TextWatcher
    private var savedIndustryList = ArrayList<Industry>()
    private var selectedIndustry: Industry? = null
    private lateinit var industryRecyclerView: RecyclerView
    private val viewModel by viewModel<FiltersViewModel>()
    private var hasInternet: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        industryAdapter = initIndustryAdapter()
        debounce()
        initListeners()
        getIndustry()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.chooseIndustryResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                ChooseIndustryResult.EmptyResult -> showInternetError()
                is ChooseIndustryResult.Error -> showInternetError()
                ChooseIndustryResult.NoInternet -> showInternetError()
                is ChooseIndustryResult.Success -> {
                    hideInternetError()
                    showIndustry(state.response)
                }
            }
        }
    }

    private fun initIndustryAdapter(): IndustryAdapter {
        val industryText = arguments?.getString("INDUSTRY_TEXT")
        industryRecyclerView = binding.industryRecycler
        industryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        industryAdapter = IndustryAdapter(
            industryList,
            { industry ->
                selectedIndustry = industry
            },
            industryText
        )

        industryRecyclerView.adapter = industryAdapter
        return industryRecyclerView.adapter as IndustryAdapter
    }

    private fun debounce() {
        debounce = debounce<String>(
            SEARCH_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true,
            action = { text ->
                if (text.isNullOrEmpty()) {
                    hideErrorNothing()
                    industryList.clear()
                    industryListFilter.clear()
                    industryList.addAll(savedIndustryList)
                    industryAdapter.notifyDataSetChanged()
                } else {
                    industryList.clear()
                    industryListFilter.clear()
                    savedIndustryList.forEach {
                        if (it.name.lowercase() == text.lowercase()) {
                            industryListFilter.add(it)
                        }
                    }
                    if (industryListFilter.isEmpty()) {
                        showErrorNothing()
                    } else {
                        hideErrorNothing()
                    }
                    industryList.addAll(industryListFilter)
                    industryAdapter.notifyDataSetChanged()
                    binding.industryEditText.isFocusable = false
                    binding.industryEditText.isFocusableInTouchMode = true
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                }
            })
    }

    private fun initListeners() {
        binding.selectBtn.visibility = View.GONE
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        industryAdapter.itemClickListener = { position, vacancy ->
            industryAdapter.notifyDataSetChanged()
            binding.selectBtn.visibility = View.VISIBLE
        }
        binding.selectBtn.setOnClickListener {
            if (selectedIndustry == null) {
                return@setOnClickListener
            }
            btSelectClicked(selectedIndustry!!)
        }
        binding.iconSearch.setOnClickListener {
            binding.industryEditText.text.clear()
        }
        binding.industryEditText.addTextChangedListener {
            changeSearchField()
            if (!binding.industryEditText.text.isNullOrEmpty() || hasInternet) {
                debounceSearch(binding.industryEditText.text.toString())
            }
        }
        binding.industryEditText.setOnFocusChangeListener { _, _ ->
            changeSearchField()
        }
        binding.industryEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!binding.industryEditText.text.isNullOrEmpty()) {
                    debounceSearch(binding.industryEditText.text.toString())
                }
                binding.industryEditText.isFocusable = false
                binding.industryEditText.isFocusableInTouchMode = true
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun btSelectClicked(industry: Industry) {
        setFragmentResult(
            KEY_INDUSTRY,
            bundleOf(INDUSTRY to Json.encodeToString(industry))
        )
        findNavController().navigateUp()
    }

    private fun debounceSearch(p0: String) {
        debounce?.let { it(p0) }
    }

    private fun getIndustry() {
        viewModel.getIndustry()
    }

    private fun hideErrorNothing() {
        binding.selectBtn.visibility = View.VISIBLE
        binding.nothingSearchImage.visibility = View.GONE
        binding.nothingSearchText.visibility = View.GONE
    }

    private fun showErrorNothing() {
        binding.selectBtn.visibility = View.GONE
        binding.nothingSearchImage.visibility = View.VISIBLE
        binding.nothingSearchText.visibility = View.VISIBLE
    }

    private fun showInternetError() {
        hasInternet = false
        binding.noInternetImage.visibility = View.VISIBLE
        binding.noInternetText.visibility = View.VISIBLE
    }

    private fun hideInternetError() {
        hasInternet = true
        binding.noInternetImage.visibility = View.GONE
        binding.noInternetText.visibility = View.GONE
    }

    private fun showIndustry(industry: List<Industry>) {
        industryList.clear()
        industryList.addAll(industry)
        savedIndustryList.addAll(industry)
        industryAdapter.notifyDataSetChanged()
    }
    private fun changeSearchField() {
        if (binding.industryEditText.text.isEmpty()) {
            binding.iconSearch.setImageResource(R.drawable.ic_search)
            binding.iconSearch.isEnabled = false
        } else {
            if (!binding.industryEditText.isFocused) {
                binding.iconSearch.setImageResource(R.drawable.ic_search)
                binding.iconSearch.isEnabled = false
            } else {
                binding.iconSearch.setImageResource(R.drawable.ic_clear)
                binding.iconSearch.isEnabled = true
            }
        }
    }


}