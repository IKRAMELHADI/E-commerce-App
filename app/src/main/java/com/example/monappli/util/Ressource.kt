package com.example.monappli.util

sealed class Ressource<T> (
    val data : T?=null,
    val message : String?=null
){
    class Success<T>(data: T):Ressource<T>(data)
    class Error<T>(message :String):Ressource<T>(message=message)
    class Loading<T>:Ressource<T>()

}