package com.example.kotlinfirebaseinsta.Model



data class User(
    val email : String,
    val username : String,
    val password : String,
    var photoUrl : String = ""
)
