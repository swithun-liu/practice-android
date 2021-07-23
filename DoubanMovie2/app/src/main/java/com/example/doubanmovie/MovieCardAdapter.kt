package com.example.doubanmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.doubanmovie.databinding.MovieItemBinding
import com.example.doubanmovie.entity.Episode

class MovieCardAdapter(private val dataSet: List<Episode>) :
    RecyclerView.Adapter<MovieCardAdapter.ViewHolder>() {

    // listener
    private lateinit var onItemClickListener: OnItemClickListener

    inner class ViewHolder(itemView: View, val movieItemBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = DataBindingUtil.inflate<MovieItemBinding>(
            LayoutInflater.from(parent.context), R.layout.movie_item, parent, false
        )
        return ViewHolder(binding.root, binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieItemBinding.episodeInfo = dataSet[position]
        holder.movieItemBinding.movieCover.load(dataSet[position].cover)
        // listener
        holder.movieItemBinding.movieCover.setOnClickListener{
            onItemClickListener?.onItemClick(holder.itemView, position)
        }
        holder.movieItemBinding.movieCover.setOnLongClickListener{
            onItemClickListener?.onItemLongClick(holder.itemView, position)
            true
        }

    }

    override fun getItemCount() = dataSet.size

    // listener
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    // listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}