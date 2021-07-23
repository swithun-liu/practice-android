package com.example.doubanmovie.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.doubanmovie.R.id
import com.example.doubanmovie.R.layout
import com.example.doubanmovie.logic.model.Episode
import com.example.doubanmovie.ui.adapter.MovieCardAdapter.ViewHolder

class MovieCardAdapter() : RecyclerView.Adapter<ViewHolder>() {

    private var dataSet: List<Episode> = mutableListOf()

    // listener
    private lateinit var onItemClickListener: OnItemClickListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieName: TextView = view.findViewById(id.movieName)
        val movieRate: TextView = view.findViewById(id.movieRate)
        val movieCover: ImageView = view.findViewById(id.movieCover)
    }

    fun setDataSet(data: List<Episode>) {
        dataSet = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.movie_item, parent, false)
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