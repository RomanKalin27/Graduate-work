package ru.practicum.android.diploma.filters.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.data.dto.models.CountryDTO

class CountryAdapter(private val clickListener: CountryClickListener) :
    RecyclerView.Adapter<CountryViewHolder>() {
    var countryList = ArrayList<CountryDTO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = countryList.size
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(countryList[position]) }
    }
}

fun interface CountryClickListener {
    fun onTrackClick(country: CountryDTO)
}