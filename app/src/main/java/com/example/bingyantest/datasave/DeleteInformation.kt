package com.example.bingyantest.datasave

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DeleteInformation  (val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createApplys = "create table DeleteInformation (" +
            " id integer primary key autoincrement," +
            "sender text," +
            "receiver text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createApplys)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}