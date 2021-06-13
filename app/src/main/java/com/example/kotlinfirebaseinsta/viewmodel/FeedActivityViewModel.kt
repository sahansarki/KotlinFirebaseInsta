package com.example.kotlinfirebaseinsta.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.kotlinfirebaseinsta.FireBaseService.FirebaseLogic
import com.example.kotlinfirebaseinsta.Model.Post
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedActivityViewModel(application: Application) : BaseViewModel(application) {

    private val applicationFeed = application

    private var auth: FirebaseAuth = FirebaseLogic.auth
    private var db: FirebaseFirestore = FirebaseLogic.db

    var userPosts = MutableLiveData<ArrayList<Post>>()


    fun getDataFromFirestore() {
        db.collection("Posts").orderBy(
            "date",
            Query.Direction.DESCENDING
        ).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(
                    applicationFeed,
                    exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG
                ).show()

            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val postsList = ArrayList<Post>()
                        val documents = snapshot.documents
                        for (document in documents) {
                            val post = Post(
                                document.get("comment").toString(),
                                document.get("date") as Timestamp,
                                document.get("downloadUrl") as String,
                                document.get("userEmail") as String
                            )
                            postsList.add(post)

                        }

                        userPosts.postValue(postsList)


                    }
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

}