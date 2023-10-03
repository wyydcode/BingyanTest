package com.example.bingyantest.datasave

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createChats= "create table Chats (" +
            " id integer primary key autoincrement," +
            "sender text," +
            "receiver text," +
            "content text,"+
            "date text)"
    private val createUsers = "create table Users (" +
            " id integer primary key autoincrement," +
            "name text," +
            "account text," +
            "password text," +
            "email text," +
            "imageuri text)"
    private val createApplys = "create table SolvedApplys (" +
            " id integer primary key autoincrement," +
            "sender text," +
            "receiver text,"+
            "ifagree integer)"
    private val createUnApplys = "create table UnSolvedApplys (" +
            " id integer primary key autoincrement," +
            "sender text," +
            "receiver text)"
    private val createFriends = "create table Friends (" +
            " id integer primary key autoincrement," +
            "user text," +
            "name text," +
            "account text," +
            "email text," +
            "imageuri text)"
    private val createDeleteApplys = "create table DeleteInformation (" +
            " id integer primary key autoincrement," +
            "sender text," +
            "receiver text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createUsers)
        db.execSQL(createFriends)
        db.execSQL(createApplys)
        db.execSQL(createUnApplys)
        db.execSQL(createDeleteApplys)
        db.execSQL(createChats)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}