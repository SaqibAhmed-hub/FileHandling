package com.example.filehandling.adapter

import android.content.*
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filehandling.R
import com.example.filehandling.model.MediaStoreImage

class MainAdapter(private val context: Context,private val image: List<MediaStoreImage>) :RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: MediaStoreImage) {
            val imageView = itemView.findViewById<ImageView>(R.id.image_view)
            imageView.load(data.contentUri)
            itemView.setOnClickListener {
//                renameFile(context,data.contentUri,"saq1")
                try{
                    val  resolver = context.applicationContext.contentResolver
                    val mediaID = data.id
                    val selection = "${MediaStore.Images.Media._ID} = ?"
                    val uri = MediaStore.getDocumentUri(context,data.contentUri)
                    val songDetails = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, "saqib.jpg")
                    }
                    val songupdated = resolver.update(uri!!,songDetails,null,null)
                }catch (e: java.lang.Exception){
                    Log.i("TAG", "bind: $e")
                }

            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun renameFile(context: Context, contentUri: Uri, s: String) {
            val contentresolver = context.contentResolver
            val contentValue = ContentValues()

            try {
                contentValue.put(MediaStore.Images.Media.IS_PENDING,1)
                contentresolver.update(contentUri,contentValue,null,null)
                contentValue.clear()
                contentValue.put(MediaStore.Images.Media.DISPLAY_NAME, "$s.jpg")
                contentValue.put(MediaStore.Images.Media.IS_PENDING,0)
                contentresolver.update(contentUri,contentValue,null,null)

////                contentresolver.takePersistableUriPermission(contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                val docUri = MediaStore.getDocumentUri(context,contentUri)
//                DocumentsContract.renameDocument(contentresolver,docUri!!,"saqi")

            }catch (e:Exception){
                println(e)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.include_media_query,parent,false)
        return ViewHolder(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val data = image[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return image.size
    }


}
