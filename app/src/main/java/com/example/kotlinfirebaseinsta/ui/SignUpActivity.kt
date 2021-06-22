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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinfirebaseinsta.R
import com.example.kotlinfirebaseinsta.viewmodel.SignUpActivityViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_upload.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpActivityViewModel
    private var temporaryView : View? = null
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

            if (granted) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultLauncher.launch(intent)
            }
        }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                signUpViewModel.userPhoto = result.data?.data
                signUpViewModel.photoClick(temporaryView!!)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpViewModel = ViewModelProvider(this).get(SignUpActivityViewModel::class.java)

        observeIntent()


    }

    fun imageClick(view: View) {

        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        temporaryView = view
    }


    fun signUp(View: View) {
        signUpViewModel.signUp(View)

    }

    private fun observeIntent() {
        signUpViewModel.signIntent.observe({
            lifecycle
        }, { intent ->
            intent?.let {
                startActivity(intent)
                finish()
            }

        })
    }
}