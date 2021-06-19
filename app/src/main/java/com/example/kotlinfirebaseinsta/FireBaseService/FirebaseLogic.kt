package com.example.kotlinfirebaseinsta.FireBaseService

import android.widget.Toast
import com.example.kotlinfirebaseinsta.Model.Post
import com.example.kotlinfirebaseinsta.Model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

object FirebaseLogic {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var storage: FirebaseStorage = FirebaseStorage.getInstance()


    fun updateLikeNumber(post: Post, email: String, positive: Boolean) {

        db.collection("Posts").document(email).update("likeNumber", post.likeNumber)
        if (positive) {
            db.collection("Posts").document(email)
                .update("users", FieldValue.arrayUnion(auth.currentUser?.email))
        } else {
            db.collection("Posts").document(email).update("users", FieldValue.arrayRemove(auth.currentUser?.email))
        }

    }

}