package com.example.quanlytaichinh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytaichinh.OnClickListener
import com.example.quanlytaichinh.R
import com.example.quanlytaichinh.databinding.ItemSpendBinding
import com.example.quanlytaichinh.model.Spend

class SpendAdapter(val listener: OnClickListener<Spend>): ListAdapter<Spend, SpendAdapter.ViewHolder>(SpendDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    class ViewHolder(val binding: ItemSpendBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Spend,
            listener: OnClickListener<Spend>
        ) {

            binding.tvCode.text = "#${item.spendId}"
            when(item.type) {
                0 -> {
                    binding.tvCategory.text = "THU"
                    setColor(R.color.green)
                }
                1 -> {
                    binding.tvCategory.text = "CHI"
                    setColor(R.color.red)
                }
            }
            binding.clContainer.setOnClickListener {
                listener.onClick(item)
            }

            binding.viewModel = item
            binding.executePendingBindings()
        }

        private fun setColor(color: Int) {
            binding.textView5.setTextColor(binding.root.resources.getColor(color))
            binding.textView6.setTextColor(binding.root.resources.getColor(color))
            binding.textView7.setTextColor(binding.root.resources.getColor(color))
            binding.textView8.setTextColor(binding.root.resources.getColor(color))
            binding.textView9.setTextColor(binding.root.resources.getColor(color))
            binding.tvCode.setTextColor(binding.root.resources.getColor(color))
            binding.tvTime.setTextColor(binding.root.resources.getColor(color))
            binding.tvCategory.setTextColor(binding.root.resources.getColor(color))
            binding.tvContent.setTextColor(binding.root.resources.getColor(color))
            binding.tvCost.setTextColor(binding.root.resources.getColor(color))
            binding.tvSection.setTextColor(binding.root.resources.getColor(color))
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemSpendBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class SpendDiffCallback: DiffUtil.ItemCallback<Spend>() {
    override fun areItemsTheSame(oldItem: Spend, newItem: Spend): Boolean {
        return oldItem.spendId == newItem.spendId
    }

    override fun areContentsTheSame(oldItem: Spend, newItem: Spend): Boolean {
        return oldItem == newItem
    }

}