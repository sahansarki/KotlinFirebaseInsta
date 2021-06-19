package com.example.kotlinfirebaseinsta.Model

import com.google.firebase.Timestamp


data class Post(
    val comment : String,
    val date : Timestamp?,
    val downloadUrl : String,
    val userEmail : String,
    var likeNumber : Int = 0,
    var users : List<String> = listOf()
)
