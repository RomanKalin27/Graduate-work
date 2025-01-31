package ru.practicum.android.diploma.favorites.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemSearchBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy


class FavoritesViewHolder(private val binding: ItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(vacancy: Vacancy) {
        Glide.with(itemView)
            .load(vacancy.logoUrl)
            .placeholder(R.drawable.vacancy_placeholder)
            .transform(RoundedCorners(12))
            .into(binding.image)
        binding.title.text = vacancy.title
        binding.company.text = vacancy.company
        binding.value.text = vacancy.salary
        itemView.setOnClickListener {
        }
        itemView.setOnLongClickListener {
            true
        }
    }
}