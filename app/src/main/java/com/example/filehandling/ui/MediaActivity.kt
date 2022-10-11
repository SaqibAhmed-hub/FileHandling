package com.example.filehandling.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.filehandling.R
import com.example.filehandling.adapter.MainAdapter
import com.example.filehandling.viewModel.MediaViewModel
import kotlinx.android.synthetic.main.activity_media.*

class MediaActivity : AppCompatActivity() {

    private lateinit var viewModel: MediaViewModel
    private lateinit var mediaAdapter: MainAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        viewModel = ViewModelProvider(this)[MediaViewModel::class.java]
        viewModel.loadImage()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this@MediaActivity,2)

        viewModel.image.observe(this) {
            Log.i("TAG", "onCreate: $it")
            mediaAdapter = MainAdapter(this@MediaActivity,it)
            recyclerView.adapter = mediaAdapter
        }
    }
}