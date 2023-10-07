package ru.practicum.android.diploma.filters.presentation.rv

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemRegionBinding
import ru.practicum.android.diploma.filters.data.dto.models.RegionsDTO

class RegionViewHolder(private val binding: ItemRegionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(country: RegionsDTO) {
        binding.region.text = country.name
    }

    val radioButton = binding.radioButton
}