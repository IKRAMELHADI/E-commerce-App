package com.example.monappli.data

class User (
    val firstname: String,
    val lastname: String,
    val email: String,
    var imagePath: String = ""
){
    constructor():this("","","","")
}