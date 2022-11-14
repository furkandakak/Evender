package com.furkandakak.together.model

import com.google.type.Date


data class Chat(
    val message:String,
    val nick: String,
    val documentId:String,
    val userId:String,
    val date: String
)
{

}