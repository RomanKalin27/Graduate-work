package ru.practicum.android.diploma.filters.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.domain.models.AreaDomain

class RegionAdapter(private val clickListener: RegionClickListener) :
    RecyclerView.Adapter<RegionViewHolder>() {
    var regionList = ArrayList<AreaDomain>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RegionViewHolder(
        ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun getItemCount() = regionList.size
    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(regionList[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(regionList[position])
        }
    }

    fun interface RegionClickListener {
        fun onTrackClick(country: AreaDomain)
    }
}
