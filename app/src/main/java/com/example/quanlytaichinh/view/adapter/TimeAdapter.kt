package com.example.quanlytaichinh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytaichinh.databinding.ItemTimeBinding
import com.example.quanlytaichinh.model.Time

class TimeAdapter: ListAdapter<Time, TimeAdapter.ViewHolder>(TimeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(val binding: ItemTimeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Time) {

            if(item.month == 13) {
                binding.tvMonth.text = "Cả năm"
            } else {
                binding.tvMonth.text = "Tháng ${item.month}"
            }

            binding.viewModel = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTimeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class TimeDiffCallback: DiffUtil.ItemCallback<Time>() {
    override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
        return oldItem == newItem
    }

}