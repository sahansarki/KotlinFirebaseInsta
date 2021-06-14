package com.example.kotlinfirebaseinsta.ui

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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.SignUpActivityViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_upload.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel : SignUpActivityViewModel
    var selectedPicture : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        signUpViewModel = ViewModelProvider(this).get(SignUpActivityViewModel::class.java)

        observeIntent()



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

            selectedPicture = data.data
            signUpViewModel.userPhoto = data.data

            if(selectedPicture != null) {
                if(Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(contentResolver,selectedPicture!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    user_photo.setImageBitmap(bitmap)

                } else {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedPicture!!)
                    user_photo.setImageBitmap(bitmap)
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun signUp(View: View) {
        signUpViewModel.signUp(View)

    }

    private fun observeIntent() {
        signUpViewModel.signIntent.observe({
            lifecycle
        } , { intent ->
            intent?.let {
                startActivity(intent)
                finish()
            }

        })
    }
}