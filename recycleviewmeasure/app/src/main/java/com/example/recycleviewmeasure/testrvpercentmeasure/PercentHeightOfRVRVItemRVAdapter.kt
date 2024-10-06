package com.example.recycleviewmeasure.testrvpercentmeasure

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewmeasure.databinding.PercentHeightOfRvRvItemLayoutBinding

class PercentHeightOfRVRVItemRVAdapter : ListAdapter<RVItemData, LogRVItemVH>(Differ()) {
    class Differ : DiffUtil.ItemCallback<RVItemData>() {
        override fun areItemsTheSame(oldItem: RVItemData, newItem: RVItemData): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: RVItemData, newItem: RVItemData): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogRVItemVH {
        return LogRVItemVH(
            PercentHeightOfRvRvItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                LinearLayout(parent.context),
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LogRVItemVH, position: Int) {
        getItem(position).let {
            holder.bindData(it)
        }
    }
}

class LogRVItemVH(private val binding: PercentHeightOfRvRvItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: RVItemData) {
        binding.root.heightPercentOfParent = data.percent / 100f
        binding.percentText.text = "${data.percent}%"
    }
}

class RVItemData(
    val percent: Int
)