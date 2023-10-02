package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bingyantest.R
import com.example.bingyantest.datasave.UsersDatabaseHelper
import com.example.bingyantest.objects.MyObjects
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.util.Random
import java.util.UUID

class RegisterActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val password1 = findViewById<EditText>(R.id.password1)
        val password2 = findViewById<EditText>(R.id.password2)
        val register = findViewById<Button>(R.id.register)
        register.setOnClickListener{
            var accountInt = (Random().nextInt(90000000) + 10000000)
            var account = accountInt.toString()
            if(password1.text.toString()==password2.text.toString()){
                saveData(this,account,password1.text.toString())
                AlertDialog.Builder(this).apply {
                    setTitle("注册成功")
                    setMessage("你的账号为${account}，请牢记你的账号和密码，登陆后可在个人主页完善个人信息")
                    setCancelable(false)
                    setPositiveButton("确定") { dialog, which ->//删除
                        Toast.makeText(context,"注册成功", Toast.LENGTH_SHORT).show()
                        LoginActivity.actionStart(context,account)
                    }
                    show()
                }
            }
        }
    }

    fun saveData(context: Context,account: String, password: String){
        val dbHelper = UsersDatabaseHelper(context,"Users",1)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("account",account)
            put("password",password)
        }
        db.insert("Users",null,values)
    }
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}