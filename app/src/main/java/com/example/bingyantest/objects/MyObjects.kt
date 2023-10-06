package com.example.bingyantest.objects

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.bingyantest.datasave.MyDatabaseHelper

object MyObjects  {
    public lateinit var appContext: Context
    public lateinit var dbHelper: MyDatabaseHelper
    public var newFriendAccount = ArrayList<String>()
    private var listeners = ArrayList<DataUpdateListener>()
    public var newFriendList = ArrayList<Friend>()
    public var usersList = ArrayList<Users>()
    public var friendslist = MutableLiveData<ArrayList<Friend>>()
    public var informList = ArrayList<Friend>()
    public lateinit var userAccount: String
    public lateinit var user:Users
    public var groupList = ArrayList<Group>()
    fun refreshUser(account: String):Users{
        val dbHelper = MyDatabaseHelper(appContext,"Users",1)
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
    fun queryNewFriend(account: String):Friend{
        newFriendList.forEach{
            if(account == it.account){
                return it
            }
        }
        return Friend("error","error","error","error")
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
        if(friendslist.value?.contains(friend) == true){
            return
        }
        friendslist.value?.add(friend)
        val dbHelper = MyDatabaseHelper(appContext,"Users",1)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // 开始组装第一条数据
            put("user", userAccount)
            put("groups","联系人")
            put("name", friend.name)
            put("account", friend.account)
            put("email", friend.email)
            put("imageuri", friend.imageuri)
        }
        db.insertWithOnConflict("Friends", null, values,SQLiteDatabase.CONFLICT_IGNORE)
    }
    fun remove(account: String){
        val dbHelper = MyDatabaseHelper(appContext,"Users",1)
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
        val dbHelper = MyDatabaseHelper(appContext,"Users",1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("Users",null,null,null,null,null,null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val account = cursor.getString(cursor.getColumnIndexOrThrow("account"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val imageuri = cursor.getString(cursor.getColumnIndexOrThrow("imageuri"))
                usersList.add(Users(name,account,password,imageuri,email))
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Friends
        val cursor1 = db.query("Friends", null, "user = ?", arrayOf("$userAccount"), null, null, null)
        if (cursor1.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val name = cursor1.getString(cursor1.getColumnIndexOrThrow("name"))
                val account = cursor1.getString(cursor1.getColumnIndexOrThrow("account"))
                val email = cursor1.getString(cursor1.getColumnIndexOrThrow("email"))
                val imageuri = cursor1.getString(cursor1.getColumnIndexOrThrow("imageuri"))
                val group = cursor1.getString(cursor1.getColumnIndexOrThrow("groups"))
                val friend = Friend(name,account,imageuri,email)
                var find = false
                groupList.forEach{
                    if(it.name==group){
                        it.member.add(friend)
                        find = true
                        return@forEach
                    }
                }
                if(!find&&group!=null){
                    val newGroup = Group(group,ArrayList<Friend>())
                    newGroup.member.add(friend)
                    groupList.add(newGroup)
                }
                friendslist.value?.add(friend)
            } while (cursor1.moveToNext())
        }
        cursor1.close()

        val cursor2 = db.query("UnSolvedApplys", arrayOf("sender"),"receiver = ? ", arrayOf("${userAccount}"),null,null,null)
        if(cursor2.moveToFirst()){
            do{
                newFriendAccount.add(cursor2.getString(cursor2.getColumnIndexOrThrow("sender")).toString())
            }while (cursor2.moveToNext())
        }
        cursor2.close()
        usersList.forEach{
            if(it.account in newFriendAccount){
                newFriendList.add(Friend(it.name,it.account,it.imageuri,it.email))
            }
        }
        val cursor3 = db.query("SolvedApplys", arrayOf("receiver"),"sender = ?", arrayOf("$userAccount"),null,null,null)
        if(cursor3.moveToFirst()){
            do{
                val friendaccount = cursor3.getString(cursor3.getColumnIndexOrThrow("receiver")).toString()
                usersList.forEach{
                    if(friendaccount==it.account){
                        add(Friend(it.name,it.account,it.imageuri,it.email))
                        return@forEach
                    }
                }
                db.delete("SolvedApplys","sender = ?", arrayOf("$userAccount"))
            }while (cursor3.moveToNext())
        }
        val cursor4 = db.query("DeleteInformation", arrayOf("sender"),"receiver = ?", arrayOf("$userAccount"),null,null,null)
        if(cursor4.moveToFirst()){
            do{
                val sender = cursor4.getString(cursor4.getColumnIndexOrThrow("sender"))
                remove(sender)
                informList.add(query(sender))
            }while (cursor4.moveToNext())
        }
    }
    fun initialize(context: Context){
        appContext = context.applicationContext
        dbHelper = MyDatabaseHelper(appContext,"Users",1)
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
    fun getUriFromDrawableRes(context: Context, id: Int): Uri {
        val resources = context.resources;
        val path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ resources.getResourcePackageName(id) + "/"+ resources.getResourceTypeName(id) + "/"+ resources.getResourceEntryName(id)
        return Uri.parse(path);
    }
    fun reset() {
        newFriendAccount.clear()
        listeners.clear()
        newFriendList.clear()
        usersList.clear()
        friendslist.value?.clear()
        informList.clear()
        userAccount = ""
        groupList.clear()
    }

    interface DataUpdateListener{
        fun onDataUpdate()
    }
}