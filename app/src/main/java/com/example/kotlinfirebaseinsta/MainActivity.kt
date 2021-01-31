package com.example.kotlinfirebaseinsta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()


    }

    fun signUp(view : View) {
        val email = findViewById<TextView>(R.id.userEmailText).text.toString()
        val password = findViewById<TextView>(R.id.userPasswordText).text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful) {
                val intent = Intent(applicationContext,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.addOnFailureListener {
            if ( it != null) {
                Toast.makeText(applicationContext,it.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signIn(view : View) {

    }

}