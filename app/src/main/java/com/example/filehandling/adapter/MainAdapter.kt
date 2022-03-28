package com.example.filehandling.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filehandling.R
import com.example.filehandling.model.MediaStoreImage

class MainAdapter(private val image: List<MediaStoreImage>) :RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(data: MediaStoreImage) {
            val imageView = itemView.findViewById<ImageView>(R.id.image_view)
            imageView.load(data.contentUri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.include_media_query,parent,false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val data = image[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return image.size
    }


}
