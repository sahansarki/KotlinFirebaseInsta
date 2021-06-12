package com.example.kotlinfirebaseinsta.FireBaseService

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseLogic {

     var db : FirebaseFirestore = FirebaseFirestore.getInstance()
     var auth : FirebaseAuth = FirebaseAuth.getInstance()




}