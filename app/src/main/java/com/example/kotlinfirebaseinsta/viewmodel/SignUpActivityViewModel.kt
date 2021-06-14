package com.example.kotlinfirebaseinsta.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.Model.User
import com.example.kotlinfirebaseinsta.ui.FeedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import java.util.*

class SignUpActivityViewModel(application: Application) : BaseViewModel(application) {

    private val applicationSign = application
    private var auth: FirebaseAuth = FirebaseLogic.auth
    private var db: FirebaseFirestore = FirebaseLogic.db
    private var storage: FirebaseStorage = FirebaseLogic.storage
    var signIntent = MutableLiveData<Intent>()

    private lateinit var email: String
    private lateinit var username: String
    private lateinit var password: String
    var userPhoto: Uri? = null

    fun signUp(view: View) {

        email = view.rootView.user_Usermail.text.toString()
        username = view.rootView.user_Username.text.toString()
        password = view.rootView.user_Userpassword.text.toString()


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                putImageUrl()
                val intent = Intent(applicationSign, FeedActivity::class.java)
                signIntent.value = intent
            }

        }.addOnFailureListener {
            Toast.makeText(applicationSign, it.localizedMessage?.toString(), Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun putImageUrl() {

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val profilePhotoReference = storage.reference.child("ProfileImages").child(imageName)

        if (userPhoto != null) {

            profilePhotoReference.putFile(userPhoto!!).addOnSuccessListener { taskSnapshot ->

                //val uploadedPhotoReference = profilePhotoReference.child(imageName)

                profilePhotoReference.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()

                    makeUser(downloadUrl)

                    val postMap = hashMapOf<String, Any>()
                    postMap["downloadUrl"] = downloadUrl




                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationSign, exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


    private fun makeUser(photoUrl : String) {

        val user = User(email, username, password , photoUrl)
        val userMap = hashMapOf<String, Any>()


        userMap["email"] = user.email
        userMap["username"] = user.username
        userMap["password"] = user.password
        userMap["photoUrl"] = user.photoUrl

        db.collection("Users").add(userMap).addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                Toast.makeText(
                    applicationSign,
                    " ${user.username}  Sisteme Eklendi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationSign, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }


    }

}