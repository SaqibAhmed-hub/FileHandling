package com.example.filehandling.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.filehandling.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForPermission()
        btn_save.setOnClickListener(this)
        btn_read.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        btn_save_external.setOnClickListener(this)
        btn_read_external.setOnClickListener(this)
        btn_goto.setOnClickListener(this)
        btn_sharedStorage.setOnClickListener(this)

        /*
        * To create a Directory in the App Specific Storage.
        * applicationContext.getDir("MyFiles",Context.MODE_PRIVATE)
        * */

    }

    private fun saveToExternalFile() {
        val fileName = file_name.text.toString()
        val fileContent = file_content.text.toString()
        try {
            if (isExternalStorageWritable()) {
                val appSpecificExternalFile =
                    File(applicationContext.getExternalFilesDir(null), fileName)
                appSpecificExternalFile.writeText(fileContent, Charset.forName("UTF-8"))
                showToast("File Saved Successfully")
            } else {
                showToast("Cannot access to External")
            }
        } catch (e: Exception) {
            e.message?.let { showToast(it) }
        }

    }

    private fun readFromFile() {
        val fileName = file_name.text.toString()
        if (fileName.isNotEmpty()) {
            try {
                applicationContext.openFileInput(fileName).bufferedReader().use {
                    file_content.setText(it.readText())
                }
            } catch (e: FileNotFoundException) {
                showToast("File Not Found")
            } catch (e: Exception) {
                e.message?.let { showToast(it) }
            }

        } else {
            showToast("Please enter the filename")
        }
    }

    private fun saveToFile() {
        val fileName = file_name.text.toString()
        val fileContent = file_content.text.toString()
        if (fileName.isNotEmpty() && fileContent.isNotEmpty()) {
            try {
                applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                    it.write(fileContent.toByteArray())
                }
                showToast("File Save Successfully")
            } catch (e: Exception) {
                e.message?.let { showToast(it) }
            }
            clearContent()
            callListFile()
        } else {
            showToast("Please enter the filename and content")
        }
    }

    private fun callListFile() {
        val files = applicationContext.fileList()
        files.forEach {
            file_list.append("$it \n")
        }
    }

    private fun clearContent() {
        file_name.text?.clear()
        file_content.text?.clear()
    }

    //it check read and write
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // it check for read Only
    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_save -> saveToFile()
            R.id.btn_read -> readFromFile()
            R.id.btn_cancel -> clearContent()
            R.id.btn_save_external -> saveToExternalFile()
            R.id.btn_read_external -> readExternalFromFile()
            R.id.btn_goto -> navigateToActivity("media")
            R.id.btn_sharedStorage -> navigateToActivity("")
        }
    }

    private fun navigateToActivity(name: String) {
        if (name == "media") {
            Intent(this, MediaActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }else{
            Intent(this, SharedStorageActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun readExternalFromFile() {
        val fileName = file_name.text.toString()
        if (fileName.isNotEmpty()) {
            try {
                val appSpecificExternalFile =
                    File(applicationContext.getExternalFilesDir(null), fileName)
                file_content.setText(appSpecificExternalFile.readText(Charset.forName("UTF-8")))
            } catch (e: FileNotFoundException) {
                showToast("File Not Found")
            }
        } else {
            showToast("Please Enter the filename")
        }
    }

    private fun checkForPermission() {
        val permission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permission, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}