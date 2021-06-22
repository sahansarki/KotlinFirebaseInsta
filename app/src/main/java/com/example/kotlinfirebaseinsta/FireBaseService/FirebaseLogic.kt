package com.example.kotlinfirebaseinsta.FireBaseService

import android.net.Uri
import android.view.View

import com.example.kotlinfirebaseinsta.Model.Post
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload.view.*
import java.util.*

object FirebaseLogic {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var storage: FirebaseStorage = FirebaseStorage.getInstance()


    fun updateLikeNumber(post: Post, like: Long, positive: Boolean) {

        db.collection("Posts").document(post.uuid!!).update("likeNumber", like)
        if (positive) {
            db.collection("Posts").document(post.uuid)
                .update("users", FieldValue.arrayUnion(auth.currentUser?.email))
        } else {
            db.collection("Posts").document(post.uuid)
                .update("users", FieldValue.arrayRemove(auth.currentUser?.email))
        }

    }

    fun putImageToFirebase(userPhoto: Uri?, makeUser: (String) -> Unit) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val profilePhotoReference = storage.reference.child("ProfileImages").child(imageName)

        if (userPhoto != null) {

            profilePhotoReference.putFile(userPhoto).addOnSuccessListener { taskSnapshot ->

                profilePhotoReference.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()

                    makeUser(downloadUrl)



                }.addOnFailureListener { exception ->
                    println(exception.localizedMessage)
                }
            }
        }
    }

    fun putUserToFirebase(
        email: String?,
        password: String?,
        putImageUrl: () -> Unit,
        intentUpdate: () -> Unit
    ) {

        if (email == null && password == null) {
            return
        }
        auth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener {
            if (it.isSuccessful) {
                putImageUrl()
                intentUpdate()

            }

        }.addOnFailureListener {
            println(it.localizedMessage)
        }
    }

    fun putUserToFirestore(userMap: Map<String, Any>, toastNewUser: () -> Unit) {

        db.collection("Users").document("${userMap["email"]}").set(userMap)
            .addOnCompleteListener { task ->
                if (task.isComplete && task.isSuccessful) {
                    toastNewUser()

                }


            }.addOnFailureListener { exception ->
            println(exception.localizedMessage)
        }
    }

    fun uploadPostToFirestore(selectedPicture: Uri?, view : View, updateReturnValue: () -> Unit ) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"


        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)


        if(selectedPicture != null) {

            imagesReference.putFile(selectedPicture).addOnSuccessListener { taskSnapshot ->


                val uploadedPictureReference = FirebaseStorage.getInstance().reference.child("images").child((imageName))
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val uuid = UUID.randomUUID().toString()
                    val downloadUrl = uri.toString()

                    val postMap = hashMapOf<String,Any>()
                    postMap["downloadUrl"] = downloadUrl
                    postMap["userEmail"] = auth.currentUser!!.email.toString()
                    postMap["comment"] = view.rootView.commentText.text.toString()
                    postMap["likeNumber"] = 0
                    postMap["date"] = Timestamp.now()
                    postMap["users"] = listOf<Int>()
                    postMap["uuid"] = uuid


                    db.collection("Posts").document("$uuid").set(postMap).addOnCompleteListener { task ->
                        if (task.isComplete && task.isSuccessful) {
                            db.collection("Posts").document("$uuid").update("uuid",uuid)
                            db.collection("Users").document("${auth.currentUser!!.email}").update("posts", FieldValue.arrayUnion(uuid))
                            updateReturnValue()
                        }
                    }.addOnFailureListener { exception ->
                        println(exception.localizedMessage)
                    }

                }
            }


        }
    }

}



