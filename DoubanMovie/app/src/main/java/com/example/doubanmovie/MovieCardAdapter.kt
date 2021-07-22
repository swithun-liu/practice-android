package com.example.doubanmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.doubanmovie.entity.Episode

class MovieCardAdapter(private val dataSet: List<Episode>) : RecyclerView.Adapter<MovieCardAdapter.ViewHolder>() {

    // listener
    private lateinit var onItemClickListener: OnItemClickListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieName: TextView = view.findViewById(R.id.movieName)
        val movieRate: TextView = view.findViewById(R.id.movieRate)
        val movieCover: ImageView = view.findViewById(R.id.movieCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieName.text = dataSet[position].title
        holder.movieRate.text = dataSet[position].rate
        holder.movieCover.load(dataSet[position].cover)
        // listener
        holder.movieCover.setOnClickListener{
            onItemClickListener?.onItemClick(holder.itemView, position)
        }
        holder.movieCover.setOnLongClickListener{
            onItemClickListener?.onItemLongClick(holder.itemView, position)
            true
        }

    }

    override fun getItemCount() = dataSet.size

    // listener
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    // listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}