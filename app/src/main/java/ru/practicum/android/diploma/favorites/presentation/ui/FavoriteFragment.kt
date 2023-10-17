package ru.practicum.android.diploma.favorites.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.favorites.presentation.adapters.FavoritesAdapter
import ru.practicum.android.diploma.favorites.presentation.view_model.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModel<FavoriteViewModel>()
    private val vacancyList = ArrayList<Vacancy>()
    private var favoriteAdapter = FavoritesAdapter(vacancyList)
    private var onVacancyClickDebounce: ((Vacancy) -> Unit)? = null
    private var onLongVacancyClickDebounce: ((Vacancy) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        observeOnContentState()
//        initAdapter()
//        initListeners()
//        viewModel.fillData()
    }

    override fun onResume() {
        super.onResume()
//        viewModel.fillData()
    }

//    private fun observeOnContentState() {
//        viewModel.observeContentState().observe(viewLifecycleOwner) { contentState ->
//            render(contentState)
//        }
//    }
//
//    private fun render(state: FavoritesScreenState) {
//        when (state) {
//            is FavoritesScreenState.Content -> showContent(state.list)
//            FavoritesScreenState.Empty -> showMessage()
//        }
//    }
//
//    private fun showMessage() {
//        binding.placeHolderImage.visibility = View.VISIBLE
//        binding.placeHolderText.visibility = View.VISIBLE
//    }
//
//    private fun showContent(listVacancy: List<Vacancy>) {
//        favoriteAdapter?.apply {
//            vacancyList.clear()
//            vacancyList.addAll(listVacancy)
//            notifyDataSetChanged()
//        }
//        with(binding) {
//            placeHolder.visibility = View.GONE
//            recycler.visibility= View.VISIBLE
//        }
//
//    }
//
//    private fun initListeners(){
//
//        favoriteAdapter.itemClickListener = { position, track ->
//            onVacancyClickDebounce?.let { it(track) }
//        }
//
//        favoriteAdapter.itemLongClickListener = { position, track ->
//            onLongVacancyClickDebounce?.let { it(track) }
//        }
//
//        onVacancyClickDebounce = debounce<Vacancy>(CLICK_DEBOUNCE_DELAY_MILLIS,
//            coroutineScope = viewLifecycleOwner.lifecycleScope,
//            useLastParam = false)  { vacancy ->
//            // Навигация на экран Вакансии
//        }
//
//        onLongVacancyClickDebounce = debounce<Vacancy>(CLICK_DEBOUNCE_DELAY_MILLIS,
//            coroutineScope = viewLifecycleOwner.lifecycleScope,
//            useLastParam = false)  { vacancy ->
//            showDialog(vacancy)
//        }
//
//    }
//    private fun showDialog(vacancy: Vacancy) {
//        MaterialAlertDialogBuilder(requireContext())
//            .setTitle(getString(R.string.delete_vacancy))
//            .setNegativeButton(getString(R.string.no)) { _, _ -> }
//            .setPositiveButton(getString(R.string.yes)) { _, _ ->
//
//                viewModel.removeVacancy(vacancy.id)
//            }
//            .show()
//    }
//
//    private fun initAdapter() {
//        binding.recycler.apply {
//            adapter = favoriteAdapter
//            layoutManager = LinearLayoutManager(context)
//        }
//    }
//    private fun goBack() {
//        findNavController().navigateUp()
//    }

}