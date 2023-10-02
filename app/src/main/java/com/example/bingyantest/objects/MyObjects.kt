package com.example.bingyantest.objects

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.bingyantest.activity.NewFriend
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.datasave.UsersDatabaseHelper

object MyObjects  {
    public lateinit var appContext: Context
    private lateinit var dbHelper: MyDatabaseHelper
    private var listeners = ArrayList<DataUpdateListener>()
    public var newFriendList = ArrayList<Friend>()
    public var friendslist = MutableLiveData<ArrayList<Friend>>()
    public lateinit var userAccount: String
    public lateinit var user:Users
    fun refreshUser(account: String):Users{
        val dbHelper = UsersDatabaseHelper(appContext,"Users",1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("Users", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val _account = cursor.getString(cursor.getColumnIndexOrThrow("account"))?:"null"
                if(account!=_account)   continue
                val _name = cursor.getString(cursor.getColumnIndexOrThrow("name"))?:"null"
                val _password = cursor.getString(cursor.getColumnIndexOrThrow("password"))?:"null"
                val _email = cursor.getString(cursor.getColumnIndexOrThrow("email"))?:"null"
                val _imageuri = cursor.getString(cursor.getColumnIndexOrThrow("imageuri"))?:"null"
                cursor.close()
                return Users(_name,account,_password,_imageuri,_email)
            } while (cursor.moveToNext())
        }
        return user
    }
    fun query(account: String):Friend{
        friendslist.value?.forEach{
            if(account == it.account){
                return it
            }
        }
        return Friend("error","error","error","error")
    }
    fun add(friend:Friend){
        friendslist.value?.add(friend)
        dbHelper = MyDatabaseHelper(appContext,"Friends",1)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // 开始组装第一条数据
            put("name", friend.name)
            put("account", friend.account)
            put("email", friend.email)
            put("imageuri", friend.imageuri)
        }
        db.insert("Friends", null, values)
    }
    fun remove(account: String){
        friendslist.value?.forEach{
            if(account == it.account){
                friendslist.value?.remove(it)
                val db = dbHelper.writableDatabase
                db.delete("Friends", "account = ?", arrayOf("${it.account}"))
                return
            }
        }

    }
    fun load(){
        val db = dbHelper.writableDatabase
        // 查询Book表中所有的数据
        val cursor = db.query("Friends", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val account = cursor.getString(cursor.getColumnIndexOrThrow("account"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val imageuri = cursor.getString(cursor.getColumnIndexOrThrow("imageuri"))
                friendslist.value?.add(Friend(name,account,imageuri,email))
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
        friendslist.value?.forEach{
            if(account == it.account){
                return true
            }
        }
        return false
    }
    fun updateData(){
        notifyDataUpdate()
    }
    fun registerDataUpdateListener(listener: DataUpdateListener){
        listeners.add(listener)
    }
    fun unregisterDataUpdateListener(listener: DataUpdateListener){
        listeners.remove((listener))
    }
    fun notifyDataUpdate(){
        for(listener in listeners){
            listener.onDataUpdate()
        }
    }
    interface DataUpdateListener{
        fun onDataUpdate()
    }
}