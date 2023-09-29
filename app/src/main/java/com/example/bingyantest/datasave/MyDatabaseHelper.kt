package com.example.bingyantest.datasave

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createFriends = "create table Book (" +
            " id integer primary key autoincrement," +
            "name text," +
            "account text," +
            "email text," +
            "imageuri text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createFriends)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}