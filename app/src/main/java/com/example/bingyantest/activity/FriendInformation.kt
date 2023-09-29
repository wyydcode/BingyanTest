package com.example.bingyantest.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R

class FriendInformation :AppCompatActivity(){
    companion object {
        fun actionStart(context: Context, data1: String, data2: String, data3: String, data4: String) {
            val intent = Intent(context, FriendInformation::class.java)
            intent.putExtra("data1", data1)
            intent.putExtra("data2", data2)
            intent.putExtra("data3",data3)
            intent.putExtra("data4",data4)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_information)
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