package com.example.model

data class Profile(
    val name:String? = null,
    val email:String? = null,
    val image:String? = null,
){
    fun isProfileEmpty():Boolean = name.isNullOrEmpty() && email.isNullOrEmpty()

}