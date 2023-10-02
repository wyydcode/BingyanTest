package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.UsersDatabaseHelper
import com.example.bingyantest.objects.MyObjects

class UserInformation : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_information)
        val name = findViewById<EditText>(R.id.user_name)
        val account = findViewById<EditText>(R.id.user_account)
        val email = findViewById<EditText>(R.id.user_email)
        val image = findViewById<ImageView>(R.id.user_image)
        name.setText(MyObjects.user.name)
        account.setText(MyObjects.userAccount)
        email.setText(MyObjects.user.email)
        val logout = findViewById<Button>(R.id.logout)
        val changeInformation = findViewById<Button>(R.id.change_information)
        changeInformation.setOnClickListener{
            MyObjects.user.email = email.text.toString()
            MyObjects.user.name = name.text.toString()
            val dbHelper = UsersDatabaseHelper(this,"Users",1)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("email",email.toString())
                put("name",name.toString())
            }
            db.update("Users",values,"account = ?", arrayOf("${MyObjects.userAccount}"))
        }
        logout.setOnClickListener{
            //退出登录
        }
    }
    companion object {
        fun actionStart(context: Context, account: String) {
            val intent = Intent(context,UserInformation::class.java)
            context.startActivity(intent)
        }
    }
}