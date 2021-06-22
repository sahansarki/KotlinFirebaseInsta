package com.example.kotlinfirebaseinsta.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.UploadActivityViewModel


class UploadActivity : AppCompatActivity() {

    private lateinit var uploadViewModel : UploadActivityViewModel
    private var temporaryView : View? = null
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

        if(granted) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if(result.resultCode == Activity.RESULT_OK) {
            uploadViewModel.selectedPicture = result.data?.data

            if(temporaryView != null) {
                uploadViewModel.photoClick(temporaryView!!)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadViewModel = ViewModelProvider(this).get(UploadActivityViewModel::class.java)


        observeReturnValue()
    }

    fun imageClick(view : View) {

        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        temporaryView = view

    }


    fun uploadClick(View : View) {

        uploadViewModel.uploadClick(View)

    }

    private fun observeReturnValue() {
        uploadViewModel.returnValue.observe({
            lifecycle
        } , {   returnvalue ->
            returnvalue?.let {
                if(returnvalue == true) {
                    finish()
                }
            }

        })
    }



}