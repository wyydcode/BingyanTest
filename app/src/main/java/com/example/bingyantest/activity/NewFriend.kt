package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.objects.MyObjects

class NewFriend:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_friend)
        val account = intent.getStringExtra("account").toString()
        val agree = findViewById<Button>(R.id.agree)
        val dbHelper = MyDatabaseHelper(this,"Users",1)
        val db = dbHelper.writableDatabase
        agree.setOnClickListener{
            val values = ContentValues().apply {
                put("sender",account)
                put("receiver",MyObjects.userAccount)
                put("ifagreed",1)
            }
            db.insertWithOnConflict("SolvedApplys",null,values,SQLiteDatabase.CONFLICT_IGNORE)
            db.delete("UnSolvedApplys","sender = ? && receiver = ?", arrayOf("$account","${MyObjects.userAccount}"))

            MyObjects.add(MyObjects.queryNewFriend(account))
        }
        val refuse = findViewById<Button>(R.id.refuse)
        refuse.setOnClickListener{
            val values = ContentValues().apply {
                put("sender",account)
                put("receiver",MyObjects.userAccount)
                put("ifagreed",0)
            }
            db.insertWithOnConflict("SolvedApplys",null,values,SQLiteDatabase.CONFLICT_IGNORE)
            db.delete("UnSolvedApplys","sender = ? && receiver = ?", arrayOf("$account","${MyObjects.userAccount}"))
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