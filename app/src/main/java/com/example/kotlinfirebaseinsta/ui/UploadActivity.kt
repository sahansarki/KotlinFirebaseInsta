package com.example.kotlinfirebaseinsta.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.UploadActivityViewModel
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : AppCompatActivity() {

    private lateinit var uploadViewModel : UploadActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadViewModel = ViewModelProvider(this).get(UploadActivityViewModel::class.java)


        observeReturnValue()
    }

    fun imageClick(view : View) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent , 2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode == 1) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent , 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            uploadViewModel.selectedPicture = data.data

            if(uploadViewModel.selectedPicture != null) {
                if(Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(contentResolver,uploadViewModel.selectedPicture!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    uploadImageView.setImageBitmap(bitmap)

                } else {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uploadViewModel.selectedPicture!!)
                    uploadImageView.setImageBitmap(bitmap)
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
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