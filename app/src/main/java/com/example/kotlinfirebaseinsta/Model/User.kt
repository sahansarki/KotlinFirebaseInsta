package com.example.kotlinfirebaseinsta.Model

import java.io.Serializable


data class User(
    val email : String,
    val username : String,
    val password : String,
    var photoUrl : String = ""
) : Serializable

