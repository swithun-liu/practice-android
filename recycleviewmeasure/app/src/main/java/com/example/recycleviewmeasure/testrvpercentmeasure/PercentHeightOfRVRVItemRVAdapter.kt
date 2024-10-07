package com.example.recycleviewmeasure.testrvpercentmeasure

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        return when (viewType) {
            0 -> LogRVItemVH.PercentItemLogRVItemVH(
                PercentHeightOfRvRvItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    LinearLayout(parent.context),
                    false
                )
            )

            else -> LogRVItemVH.OtherItem(TextView(parent.context))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RVItemData.OtherItem -> 1
            is RVItemData.PercentItem -> 0
        }
    }

    override fun onBindViewHolder(holder: LogRVItemVH, position: Int) {
        getItem(position).let {
            holder.bindData(it)
        }
    }
}

sealed class LogRVItemVH(root: View) : RecyclerView.ViewHolder(root) {
    abstract fun bindData(data: RVItemData)

    class PercentItemLogRVItemVH(private val binding: PercentHeightOfRvRvItemLayoutBinding) :
        LogRVItemVH(binding.root) {
        override fun bindData(data: RVItemData) {
            (data as? RVItemData.PercentItem)?.let {
                binding.root.heightPercentOfParent = data.percent / 100f
                binding.percentText.text = "${data.percent}%"
            }
        }
    }

    class OtherItem(private val textView: TextView) : LogRVItemVH(textView) {
        override fun bindData(data: RVItemData) {
            (data as? RVItemData.OtherItem)?.let {
                textView.text = it.content
            }
        }
    }
}

sealed interface RVItemData {
    data class PercentItem(
        val percent: Int
    ) : RVItemData


    data class OtherItem(val content: String) : RVItemData
}