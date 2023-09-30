package com.example.bingyantest.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R

class UserInformation : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friend_information)
        val name = findViewById<TextView>(R.id.fname)
        val account = findViewById<TextView>(R.id.faccount)
        val email = findViewById<TextView>(R.id.femail)
        val image = findViewById<ImageView>(R.id.fimage)
        name.text = intent.getStringExtra("data1")
        account.text = intent.getStringExtra("data2")
        email.text = intent.getStringExtra(("data3"))
        val resourceId = intent.getIntExtra("data4",0)
        image.setImageResource(resourceId)
    }
}