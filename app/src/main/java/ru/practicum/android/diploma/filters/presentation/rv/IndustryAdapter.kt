package ru.practicum.android.diploma.filters.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemRegionBinding
import ru.practicum.android.diploma.filters.domain.models.Industry

class IndustryAdapter(
    private val items: List<Industry>,
    private val onIndustrySelected: (Industry) -> Unit,
    private var industryText: String?,
) :
    RecyclerView.Adapter<IndustryAdapter.ViewHolderIndustry>() {
    var clickedName: String? = null
    var itemClickListener: ((Int, Industry) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderIndustry {
        val binding =
            ItemRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderIndustry(binding, industryText)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderIndustry, position: Int) {
        val industry = items[position]
        holder.bind(industry)
    }

    inner class ViewHolderIndustry(
        private val binding: ItemRegionBinding,
        private var industryText: String?,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(industry: Industry) {
            binding.region.text = industry.name
            binding.radioButton.isChecked = isIndustrySelected(industry)

            itemView.setOnClickListener {
                if (industry.name != clickedName) {
                    clickedName = industry.name
                    notifyDataSetChanged()
                    onIndustrySelected(industry)
                    itemClickListener?.invoke(adapterPosition, industry)
                }
            }
        }

        private fun isIndustrySelected(industry: Industry): Boolean {
            return (clickedName == null && industry.name == industryText) || clickedName == industry.name
        }
    }
}