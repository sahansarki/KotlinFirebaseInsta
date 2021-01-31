package com.example.kotlinfirebaseinsta

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class UploadActivity : AppCompatActivity() {

    var selectedPicture : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)


    }

    fun imageClick(view: View) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent , 2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode == 1) {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent , 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data

            if( selectedPicture != null) {
                if(Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(contentResolver,selectedPicture!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    findViewById<ImageView>(R.id.uploadImageView).setImageBitmap(bitmap)

                } else {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedPicture)
                    findViewById<ImageView>(R.id.uploadImageView).setImageBitmap(bitmap)
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun uploadClick(view : View) {

    }
}