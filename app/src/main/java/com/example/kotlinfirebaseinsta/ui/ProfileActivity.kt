package com.example.kotlinfirebaseinsta.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinfirebaseinsta.Model.User
import com.example.kotlinfirebaseinsta.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val user = intent.extras?.get("user") as User

        //Toast.makeText(applicationContext, "${user.username} Profili!",Toast.LENGTH_LONG).show()

        Picasso.get().load(user.photoUrl).into(profileImageView)
        profileEmail.text = user.email
        profileName.text = user.username

    }
}