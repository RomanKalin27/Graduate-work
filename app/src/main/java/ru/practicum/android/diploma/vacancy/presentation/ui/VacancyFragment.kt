package ru.practicum.android.diploma.vacancy.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.DetailVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailnfo
import ru.practicum.android.diploma.vacancy.presentation.models.DetailsVacancyScreenState
import ru.practicum.android.diploma.vacancy.presentation.view_model.DetailVacancyViewModel

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {
    private val viewModel by viewModel<DetailVacancyViewModel>()

    private val currentVacancy by lazy { retrieveVacancy() }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveVacancy()?.let { viewModel.showDetailVacancy(it) }
        observeViewModel()
        binding.icSharing.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.putExtra(Intent.EXTRA_TEXT, "ССЫЛКА")
            share.type = "text/plane"
            requireContext().startActivity(share)
        }
        binding.vacancyContactEmailValue.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:kalinroman034@gmail.com")
                )
            )
        }
        binding.vacancyContactPhoneValue.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "8-800-555-35-35", null))
            requireContext().startActivity(call)
        }
    }

    private fun setupDefaultUI(vacancy: VacancyDetailnfo) {
        with(binding) {
            showDataContent(vacancy)
            }
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

               // CONTENT обрабатывается в комплекте с FAVORITE и UNFAVORITE
               // DetailsVacancyScreenState.CONTENT -> group.isVisible = true

                DetailsVacancyScreenState.ERROR -> placeholderServerError.isVisible = true
                DetailsVacancyScreenState.FAVORITE ->{
                    group.isVisible = true
                icFavorites.setImageResource(R.drawable.ic_favorites_on)}
                DetailsVacancyScreenState.UNFAVORITE ->  {
                    group.isVisible = true
                    binding.icFavorites.setImageResource(R.drawable.ic_favorites_on)}
            }
        }
    }

    fun retrieveVacancy(): String? {
        val idVacancy = requireArguments().getString(KEY_VACANCY)
        return idVacancy
    }

    fun showDataContent(vacancy: VacancyDetailnfo) {
        with(binding) {
            scrollView.visibility = View.VISIBLE
          //  placeHolder.visibility = View.GONE
            hideContactsIfEmpty(vacancy)
            showKeySkills(vacancy)
            val tvSchedule = StringBuilder().append(vacancy.employment).append(". ").append(vacancy.schedule).toString()
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

    companion object {
        const val KEY_VACANCY = "vacancy"

        fun createArgs(vacancy: String): Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_VACANCY, vacancy)
            return bundle
        }
    }
}