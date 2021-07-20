package com.example.doubanmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class MovieCardAdapter(private val dataSet: List<Episode>) : RecyclerView.Adapter<MovieCardAdapter.ViewHolder>() {

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
    }

    override fun getItemCount() = dataSet.size
}