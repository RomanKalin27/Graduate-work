package ru.practicum.android.diploma.filters.presentation.rv

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.databinding.ItemRegionBinding
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class RegionViewHolder(private val binding: ItemCountryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(country: Area) {
        binding.country.text = country.name
    }
}