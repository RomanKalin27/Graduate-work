package ru.practicum.android.diploma.vacancy.presentation.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.core.application.App
import ru.practicum.android.diploma.core.application.appComponent
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo
import ru.practicum.android.diploma.vacancy.presentation.models.DetailsVacancyScreenState
import ru.practicum.android.diploma.vacancy.presentation.ui.SimilarVacancyFragment.Companion.SIMILAR_VACANCY
import ru.practicum.android.diploma.vacancy.presentation.ui.SimilarVacancyFragment.Companion.SIMILAR_VACANCY_KEY
import ru.practicum.android.diploma.vacancy.presentation.view_model.DetailVacancyViewModel
import ru.practicum.android.diploma.vacancy.presentation.view_model.DetailVacancyViewModelFactory
import javax.inject.Inject

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {
    @Inject
    lateinit var vmFactory: DetailVacancyViewModelFactory
    lateinit var viewModel: DetailVacancyViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }
    override fun onAttach(context: Context) {
        context.appComponent.injectVacancyFragment(this)
        super.onAttach(context)
    }
    override fun onResume() {
        super.onResume()
        viewModel.showDetailVacancy(retrieveVacancy())
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory)[DetailVacancyViewModel::class.java]
        viewModel.showDetailVacancy(retrieveVacancy())
        binding.group.visibility = View.GONE
        observeViewModel()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.vacancyContactEmailValue.setOnClickListener {
            val emailAddress = binding.vacancyContactEmailValue.text.toString()
            val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("mailto:$emailAddress")
            )

            try {
                requireContext().startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_app_for_send),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.vacancyContactPhoneValue.setOnClickListener {
            val phoneNumber = binding.vacancyContactPhoneValue.text.toString()
            val call = Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", phoneNumber, null)
            )
            requireContext().startActivity(call)
        }
        viewModel.shareUrl.observe(viewLifecycleOwner) { url ->
            binding.icSharing.setOnClickListener {
                val share = Intent(Intent.ACTION_SEND)
                share.putExtra(Intent.EXTRA_TEXT, url)
                share.type = "text/plain"
                requireContext().startActivity(Intent.createChooser(share, "Поделиться ссылкой"))
            }
        }

        binding.similarVacanciesButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_vacancyFragment_to_similarVacancyFragment,
                SimilarVacancyFragment.createArgs(retrieveVacancy())
            )
        }
    }

    private fun setupDefaultUI(vacancy: VacancyDetailnfo) {
        showDataContent(vacancy)
        favoriteClickInit(vacancy)
        viewModel.checkFavorite(vacancy)
    }

    private fun observeViewModel() {
        viewModel.detailVacancyResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailVacancyResult.AddedToFavorite -> updateUI(DetailsVacancyScreenState.FAVORITE)
                DetailVacancyResult.EmptyResult -> updateUI(DetailsVacancyScreenState.ERROR)
                is DetailVacancyResult.Error -> updateUI(DetailsVacancyScreenState.ERROR)
                DetailVacancyResult.NoFavorite -> updateUI(DetailsVacancyScreenState.UNFAVORITE)
                DetailVacancyResult.NoInternet -> updateUI(DetailsVacancyScreenState.NO_INTERNET)
                is DetailVacancyResult.Success -> setupDefaultUI(state.response)
            }
        }
    }

    private fun updateUI(detailsVacancyScreenState: DetailsVacancyScreenState) {
        with(binding) {
            //Скрываем всё, кроме тулбара
            progressBar.isVisible = false
            placeholderServerError.isVisible = false
            noInternetPlaceholder.isVisible = false
            group.isVisible = false

            // Обработка конкретного состояния
            when (detailsVacancyScreenState) {
                DetailsVacancyScreenState.NO_INTERNET -> noInternetPlaceholder.isVisible = true
                DetailsVacancyScreenState.LOADING -> progressBar.isVisible = true
                DetailsVacancyScreenState.ERROR -> placeholderServerError.isVisible = true
                DetailsVacancyScreenState.FAVORITE -> {
                    group.isVisible = true
                    icFavorites.setImageResource(R.drawable.ic_favorites_on)
                }

                DetailsVacancyScreenState.UNFAVORITE -> {
                    group.isVisible = true
                    binding.icFavorites.setImageResource(R.drawable.ic_favorites_off)
                }
            }
        }
    }

    private fun retrieveVacancy(): String {
        var idVacancy = requireArguments().getString(KEY_VACANCY) ?: ""
        setFragmentResultListener(SIMILAR_VACANCY_KEY) { _, bundle ->
            idVacancy = bundle.getString(SIMILAR_VACANCY) ?: ""
        }
        return idVacancy
    }

    private fun showDataContent(vacancy: VacancyDetailnfo) {
        with(binding) {
            scrollView.visibility = View.VISIBLE
            //  placeHolder.visibility = View.GONE
            hideContactsIfEmpty(vacancy)
            showKeySkills(vacancy)
            val tvSchedule =
                StringBuilder().append(vacancy.employment).append(". ").append(vacancy.schedule)
                    .toString()
            val formattedDescription =
                HtmlCompat.fromHtml(vacancy.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

            //    if (vacancy.logo.isNotEmpty()) imageView.imageTintList = null

            vacancyContactPhoneValue.text = vacancy.contactPhones.joinToString("\n")
            vacancyPhoneCommentValue.text = vacancy.contactComment
            vacancyContactPersonValue.text = vacancy.contactName
            vacancyContactEmailValue.text = vacancy.contactEmail
            requiredExperienceValue.text = vacancy.experience
            scheduleValue.text = tvSchedule
            vacancyDescriptionValue.text = formattedDescription
            vacancyName.text = vacancy.title
            salary.text = vacancy.salary
            employerName.text = vacancy.company
            city.text = vacancy.area
            Glide.with(this@VacancyFragment.requireContext())
                .load(vacancy.logo)
                .placeholder(R.drawable.vacancy_placeholder)
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_12)))
                .into(placeholder)

        }
    }

    private fun hideContactsIfEmpty(vacancy: VacancyDetailnfo) {
        with(binding) {
            if (vacancy.contactName.isEmpty()) {
                vacancyContactPersonValue.visibility = View.GONE
                vacancyContactPerson.visibility = View.GONE
            }
            if (vacancy.contactEmail.isEmpty()) {
                vacancyContactEmailValue.visibility = View.GONE
                vacancyContactEmail.visibility = View.GONE
            }
            if (vacancy.contactPhones.isEmpty()) {
                vacancyContactPhoneValue.visibility = View.GONE
                vacancyContactPhone.visibility = View.GONE
            }
            if (vacancy.contactComment.isEmpty()) {
                vacancyPhoneCommentValue.visibility = View.GONE
                vacancyPhoneComment.visibility = View.GONE
            }
            if (vacancy.contactName.isEmpty() &&
                vacancy.contactEmail.isEmpty() &&
                vacancy.contactPhones.isEmpty()
            ) {
                vacancyContacts.visibility = View.GONE
            }
        }
    }

    private fun showKeySkills(vacancy: VacancyDetailnfo) {
        with(binding) {
            if (vacancy.keySkills.isEmpty()) {
                vacancyKeySkills.visibility = View.GONE
                vacancyKeySkillsValue.visibility = View.GONE
            } else {
                vacancyKeySkillsValue.text = vacancy.keySkills
            }
        }
    }

    private fun favoriteClickInit(vacancy: VacancyDetailnfo) {
        binding.icFavorites.setOnClickListener {
            viewModel.clickToFavoriteButton(vacancy)
        }
    }

    companion object {
        const val KEY_VACANCY = "vacancy"
        fun createArgs(vacancy: String): Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_VACANCY, vacancy)
            return bundle
        }
    }
}