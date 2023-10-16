package ru.practicum.android.diploma.filters.presentation.rv

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO

class CountryViewHolder(private val binding: ItemCountryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(country: AreasDTO) {
        binding.country.text = country.name
    }
}