package com.example.kotlinfirebaseinsta.Model

import com.google.firebase.Timestamp
import java.util.*


data class Post(

    val comment : String,
    val date : Timestamp?,
    val downloadUrl : String,
    val userEmail : String,
    var likeNumber : Long = 0,
    var users : List<String> = listOf(),
    var uuid: String = ""
)
