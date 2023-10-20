package com.example.model

data class Profile(
    val name:String? = null,
    val email:String? = null
){
    fun isProfileEmpty():Boolean = name==null && email==null

}