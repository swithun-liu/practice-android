package com.example.doubanmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieTypeAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MovieTypeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTypeText: TextView = itemView.findViewById(R.id.movieTypeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieTypeText.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size
}
