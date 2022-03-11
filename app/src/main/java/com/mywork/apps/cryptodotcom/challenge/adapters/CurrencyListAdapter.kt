package com.mywork.apps.cryptodotcom.challenge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.apps.cryptodotcom.challenge.data.entity.CurrencyInfo
import com.mywork.apps.cryptodotcom.challenge.databinding.FragmentItemBinding


class CurrencyListAdapter(private val onItemClickListener: (CurrencyInfo) -> Unit) :
    ListAdapter<CurrencyInfo, CurrencyListAdapter.CurrencyViewHolder>(CurrencyPickerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(
            FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) { currencyInfo -> onItemClickListener(currencyInfo) }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class CurrencyViewHolder(
        private val binding: FragmentItemBinding,
        private val onItemClicked: (CurrencyInfo) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencyInfo) {
            with(binding) {
                abbrevTextView.text = item.name.take(1)
                nameTextView.text = item.name
                symbolTextView.text = item.symbol
                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }
}


private class CurrencyPickerDiffCallback : DiffUtil.ItemCallback<CurrencyInfo>() {
    override fun areItemsTheSame(
        oldItem: CurrencyInfo,
        newItem: CurrencyInfo
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CurrencyInfo,
        newItem: CurrencyInfo
    ): Boolean =
        oldItem == newItem
}
