package com.example.filehandling.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.filehandling.R
import kotlinx.android.synthetic.main.activity_shared_storage_file.*

class SharedStorageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_storage_file)

        btn_send_text.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT,"This is a sample")
                type = "text/plain"
                startActivity(this)
            }


        }

        btn_send_image.setOnClickListener {
        }

    }
}