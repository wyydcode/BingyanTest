package com.example.bingyantest.objects.title

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.bingyantest.R
import com.example.bingyantest.activity.FriendInformation
import com.example.bingyantest.objects.Friend

class ChatTitleLayout(context: Context, attrs: AttributeSet,friend: Friend) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_chat, this)
        val titlename = findViewById<TextView>(R.id.titleText)
        titlename.text = friend.name
        val titleBack = findViewById<Button>(R.id.titleBack)
        val titleEdit = findViewById<Button>(R.id.titleEdit)//后续修改为查看个人信息
        titleBack.setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }
        titleEdit.setOnClickListener {
            Toast.makeText(context, "You clicked Edit button", Toast.LENGTH_SHORT).show()
            FriendInformation.actionStart(context,friend.name,friend.account,friend.email,friend.imageuri)
        }
    }
}