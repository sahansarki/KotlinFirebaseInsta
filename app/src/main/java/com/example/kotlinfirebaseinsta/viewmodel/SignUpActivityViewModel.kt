package com.example.kotlinfirebaseinsta.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.Model.User
import com.example.kotlinfirebaseinsta.ui.FeedActivity
import kotlinx.android.synthetic.main.activity_sign_up.view.*


class SignUpActivityViewModel(application: Application) : BaseViewModel(application) {

    private val applicationSign = application

    var signIntent = MutableLiveData<Intent>()


    private lateinit var email: String
    private lateinit var username: String
    private lateinit var password: String
    var userPhoto: Uri? = null

    fun signUp(view: View) {

        email = view.rootView.user_Usermail.text.toString()
        username = view.rootView.user_Username.text.toString()
        password = view.rootView.user_Userpassword.text.toString()


        FirebaseLogic.putUserToFirebase(email, password, ::putImageUrl) {
            val intent = Intent(applicationSign, FeedActivity::class.java)
            signIntent.value = intent
        }


    }

    fun photoClick(view: View) {
        if (userPhoto != null) {
            if (Build.VERSION.SDK_INT >= 28) {

                val source =
                    ImageDecoder.createSource(applicationSign.contentResolver, userPhoto!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                view.user_photo.setImageBitmap(bitmap)

            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    applicationSign.contentResolver,
                    userPhoto!!
                )
                view.user_photo.setImageBitmap(bitmap)
            }
        }
    }

    private fun putImageUrl() {

        FirebaseLogic.putImageToFirebase(userPhoto, ::makeUser)


    }


    private fun makeUser(photoUrl: String) {

        val user = User(email, username, password, photoUrl)
        val userMap = hashMapOf<String, Any>()


        userMap["email"] = user.email
        userMap["username"] = user.username
        userMap["password"] = user.password
        userMap["photoUrl"] = user.photoUrl
        userMap["posts"] = user.posts

        FirebaseLogic.putUserToFirestore(userMap) {
            Toast.makeText(
                applicationSign,
                "${userMap["username"]}  Sisteme Eklendi",
                Toast.LENGTH_LONG
            ).show()
        }


    }

}