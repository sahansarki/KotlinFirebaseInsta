package com.example.kotlinfirebaseinsta.viewmodel

import android.app.Application
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload.view.*
import java.util.*

class UploadActivityViewModel(application: Application) : BaseViewModel(application){

    var selectedPicture : Uri? = null
    var auth: FirebaseAuth = FirebaseLogic.auth
    var db: FirebaseFirestore = FirebaseLogic.db
    var storage : FirebaseStorage = FirebaseLogic.storage
    private var applicationUpload = application
    var returnValue = MutableLiveData<Boolean>()



    fun uploadClick(view : View) {

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        //val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)


        if(selectedPicture != null) {

            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->

                //Database - Firestore

                val uploadedPictureReference = FirebaseStorage.getInstance().reference.child("images").child((imageName))
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()

                    val postMap = hashMapOf<String,Any>()
                    postMap["downloadUrl"] = downloadUrl
                    postMap["userEmail"] = auth.currentUser!!.email.toString()
                    postMap["comment"] = view.rootView.commentText.text.toString()

                    postMap["date"] = Timestamp.now()

                    db.collection("Posts").add(postMap).addOnCompleteListener { task ->
                        if (task.isComplete && task.isSuccessful) {
                            returnValue.value = true
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationUpload,exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                }
            }


        }

    }
}