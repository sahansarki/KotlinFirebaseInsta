package com.example.kotlinfirebaseinsta.viewmodel

import android.app.Application
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.kotlinfirebaseinsta.Model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload.view.*
import java.util.*

class UploadActivityViewModel(application: Application) : BaseViewModel(application){


    var uploadApplication = application
    var selectedPicture : Uri? = null
    var returnValue = MutableLiveData<Boolean>()


    fun photoClick(view: View) {


        if (selectedPicture != null) {
            if (Build.VERSION.SDK_INT >= 28) {

                val source =
                    ImageDecoder.createSource(uploadApplication.contentResolver, selectedPicture!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                view.uploadImageView.setImageBitmap(bitmap)

            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    uploadApplication.contentResolver,
                    selectedPicture!!
                )
                view.uploadImageView.setImageBitmap(bitmap)
            }
        }

    }



    fun uploadClick(view : View) {

        FirebaseLogic.uploadPostToFirestore(selectedPicture,view) {
            returnValue.value = true
        }


    }
}