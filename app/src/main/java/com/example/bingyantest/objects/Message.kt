package com.example.bingyantest.objects

data class Message(val sender: String, val receiver: String, val content: String, val date: String, val type:Int){
    companion object {
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
    }
}