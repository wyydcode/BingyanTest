package com.example.bingyantest.objects

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.bingyantest.datasave.MyDatabaseHelper
import kotlinx.coroutines.NonDisposableHandle.parent

object Objects  {
    private lateinit var appContext: Context
    private lateinit var dbHelper: MyDatabaseHelper
    val friendslist = ArrayList<Friend>()
    private lateinit var userAccount: String
    fun add(friend:Friend){
        friendslist.add(friend)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // 开始组装第一条数据
            put("name", friend.name)
            put("account", friend.account)
            put("email", friend.email)
            put("imagerui", friend.imageuri)
        }
        db.insert("Friends", null, values)
    }
    fun remove(account: String){
        for(i in friendslist){
            if(account == i.account){
                friendslist.remove(i)
                val db = dbHelper.writableDatabase
                db.delete("Friends", "account = ?", arrayOf("${i.account}"))
                break
            }
        }

    }
    fun load(){
        val db = dbHelper.writableDatabase
        // 查询Book表中所有的数据
        val cursor = db.query("Book", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val account = cursor.getString(cursor.getColumnIndexOrThrow("account"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val imageuri = cursor.getString(cursor.getColumnIndexOrThrow("imageuri"))
                friendslist.add(Friend(name,account,imageuri,email))
            } while (cursor.moveToNext())
        }
        cursor.close()

    }
    fun initialize(context: Context){
        appContext = context.applicationContext
        dbHelper = MyDatabaseHelper(appContext,"Friends",1)
    }
    fun update(friend: Friend){
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // 开始组装第一条数据
            put("name", friend.name)
            put("account", friend.account)
            put("email", friend.email)
            put("imagerui", friend.imageuri)
        }
        db.update("Friends", values, "account = ?", arrayOf("${friend.account}"))
    }
    fun isFriend(account:String): Boolean{
        for(i in friendslist){
            if(account == i.account){
                return true
            }
        }
        return false
    }
}