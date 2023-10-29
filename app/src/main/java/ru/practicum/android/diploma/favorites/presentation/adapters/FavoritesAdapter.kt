package ru.practicum.android.diploma.favorites.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemSearchBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy


class FavoritesAdapter(private val vacancyList: List<Vacancy>) :
    RecyclerView.Adapter<FavoritesViewHolder>() {

    var itemClickListener: ((Int, Vacancy) -> Unit)? = null
    var itemLongClickListener: ((Int, Vacancy) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val vacancy = vacancyList[position]
        holder.bind(vacancy)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(position, vacancy) }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener?.invoke(position, vacancy)
            true
        }
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }
}