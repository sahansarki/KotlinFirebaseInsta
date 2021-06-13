package com.example.kotlinfirebaseinsta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseLogic.auth

        val currentUser = auth.currentUser

        if(currentUser != null) {
            val intent = Intent(applicationContext, FeedActivity::class.java)

            startActivity(intent)
            finish()
        }

    }

    fun signUp(view : View) {
        val email = userEmailText.text.toString()
        val password = userPasswordText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful) {
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage?.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun signIn(view : View) {
        val email = userEmailText.text.toString()
        val password = userPasswordText.text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful) {
                Toast.makeText(applicationContext,"Welcome ${auth.currentUser?.email.toString()}",Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

}