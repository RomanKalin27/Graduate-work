package ru.practicum.android.diploma.favorites.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.common.utils.Constants.CLICK_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.core.application.App
import ru.practicum.android.diploma.core.application.appComponent
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.favorites.presentation.adapters.FavoritesAdapter
import ru.practicum.android.diploma.favorites.presentation.models.FavoritesScreenState
import ru.practicum.android.diploma.favorites.presentation.view_model.FavoriteViewModel
import ru.practicum.android.diploma.favorites.presentation.view_model.FavoriteViewModelFactory
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.presentation.ui.VacancyFragment
import javax.inject.Inject

class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {
    @Inject
    lateinit var vmFactory: FavoriteViewModelFactory
    lateinit var viewModel: FavoriteViewModel
    private val vacancyList = ArrayList<Vacancy>()
    private var favoriteAdapter = FavoritesAdapter(vacancyList)
    private var onVacancyClickDebounce: ((Vacancy) -> Unit)? = null
    private var onLongVacancyClickDebounce: ((Vacancy) -> Unit)? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }
    override fun onAttach(context: Context) {
        context.appComponent.injectFavoriteFragment(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory)[FavoriteViewModel::class.java]
        observeOnContentState()
        initAdapter()
        initListeners()
    }

    private fun observeOnContentState() {
        viewModel.observeContentState().observe(viewLifecycleOwner) { contentState ->
            render(contentState)
        }
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Content -> showContent(state.list)
            FavoritesScreenState.Empty -> showMessage()
        }
    }

    private fun showMessage() {
        binding.placeHolderImage.visibility = View.VISIBLE
        binding.placeHolderText.visibility = View.VISIBLE
    }

    private fun showContent(listVacancy: List<Vacancy>) {
        favoriteAdapter.apply {
            vacancyList.clear()
            vacancyList.addAll(listVacancy)
            notifyDataSetChanged()
        }
        with(binding) {
            placeHolder.visibility = View.GONE
            placeHolderImage.visibility = View.GONE
            placeHolderText.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

    private fun initListeners() {

        favoriteAdapter.itemClickListener = { position, track ->
            onVacancyClickDebounce?.let { it(track) }
        }

        favoriteAdapter.itemLongClickListener = { position, track ->
            onLongVacancyClickDebounce?.let { it(track) }
        }

        onVacancyClickDebounce = debounce<Vacancy>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            cancelPrevious = false
        ) { vacancy ->
            navigateToVacancyDetail(vacancy.id)
        }

        onLongVacancyClickDebounce = debounce<Vacancy>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            cancelPrevious = false
        ) { vacancy ->
            showDialog(vacancy)
        }
    }

    private fun showDialog(vacancy: Vacancy) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_vacancy))
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.removeVacancy(vacancy.id)
                vacancyList.clear()
                favoriteAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun initAdapter() {
        binding.recycler.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun navigateToVacancyDetail(item: String) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_vacancyFragment, VacancyFragment.createArgs(item)
        )
    }

    private fun goBack() {
        findNavController().navigateUp()
    }
}