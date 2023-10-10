package ru.practicum.android.diploma.filters.presentation.rv

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.data.dto.models.ProfessionDTO

class IndustryViewHolder(private val binding: ItemCountryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(industry: ProfessionDTO) {
        binding.country.text = industry.name
    }
}