package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.SolvedApplyDatabaseHelper
import com.example.bingyantest.datasave.UnSolvedApplyDatabaseHelper
import com.example.bingyantest.objects.MyObjects

class NewFriend:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_friend)
        val agree = findViewById<Button>(R.id.agree)
        val dbHelper = SolvedApplyDatabaseHelper(this,"SolvedApplys",1)
        val db = dbHelper.writableDatabase
        agree.setOnClickListener{
            val values = ContentValues().apply {
                put("sender",intent.getStringExtra("account"))
                put("receiver",MyObjects.userAccount)
                put("ifagreed",1)
            }
            db.insert("SolvedApplys",null,values)
        }
        val refuse = findViewById<Button>(R.id.refuse)
        refuse.setOnClickListener{
            val values = ContentValues().apply {
                put("sender",intent.getStringExtra("account"))
                put("receiver",MyObjects.userAccount)
                put("ifagreed",1)
            }
            db.insert("SolvedApplys",null,values)
        }
    }
    companion object {
        fun actionStart(context: Context, name: String, account: String, email: String, imageuri: String) {
            val intent = Intent(context, NewFriend::class.java)
            intent.putExtra("name", name)
            intent.putExtra("account", account)
            intent.putExtra("email", email)
            intent.putExtra("imageuri", imageuri)
            context.startActivity(intent)
        }
    }
}