package com.example.quanlytaichinh.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlytaichinh.databinding.ItemSectionBinding
import com.example.quanlytaichinh.model.Section

class SectionAdapter : ListAdapter<Section, SectionAdapter.ViewHolder>(SectionDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    class ViewHolder(val binding: ItemSectionBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Section) {
            binding.viewModel = item

            if(item.name.equals("TT")) {
                binding.tvTitle.visibility = View.GONE
                binding.tvPercent.visibility = View.GONE
            } else {
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvPercent.visibility = View.VISIBLE
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemSectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class SectionDiffCallback: DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
        return (oldItem.total - oldItem.paid) == (newItem.total - newItem.paid)
    }

}