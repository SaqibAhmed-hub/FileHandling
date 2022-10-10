package com.example.filehandling.viewModel

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.filehandling.model.MediaStoreImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class MediaViewModel(app: Application): AndroidViewModel(app) {

    private val _images = MutableLiveData<List<MediaStoreImage>>()
    val image: LiveData<List<MediaStoreImage>> = _images


    fun loadImage(){
        viewModelScope.launch {
            val imageList = queryImage()
            _images.value = imageList
        }
    }

    /*
    * For a Query, You need few things
    * #URI
    * #Projection
    * #Selection
    * #SelectionArgs
    * #SortOrder
    */

    /* SQL Statement:
    * SELECT @projection FROM @MediaStore URI  WHERE @selection,@selectionArg ORDER BY @SortOrder
    * */

    private suspend fun queryImage(): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        withContext(Dispatchers.IO){
                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED
                )
                val selection = "${MediaStore.Images.Media.DATE_ADDED} >= ?"
                val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                Log.i("TAG", "queryImage: ${cursor.columnCount}")
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                while (cursor.moveToNext()){
                    val id = cursor.getLong(idColumn)
                    val dateModified = Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(displayNameColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    Log.i("ViewModel","${id}${displayName}")
                    val image = MediaStoreImage(id,displayName,dateModified,contentUri)
                    images += image
                    Log.i("ViewModel", "queryImage: $image")
                }
            }
        }
        return images
    }
}