package com.example.bingyantest.activity

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bingyantest.R
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.fragment.NoResultFragment
import com.example.bingyantest.fragment.RecycleViewFragment
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.MyObjects
import java.util.ArrayList

class AddFriend: BaseActivity() {
    private val friendList = ArrayList<Friend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val dbHelper = MyDatabaseHelper(this, "Users", 1)
        val backbtn = findViewById<ImageView>(R.id.searchback)
        val searchbtn = findViewById<Button>(R.id.search)
        val searchText = findViewById<EditText>(R.id.searchtext)
        val db = dbHelper.writableDatabase

        searchbtn.setOnClickListener{
            var flag = 0
            val cursor = db.query("Users",null,null,null,null,null,null)
            val inputText = searchText.text.toString()
            //val cursor = db.query("Users",
                //arrayOf("account"),"account = ?", arrayOf(("${inputText}")),null,null,null)
            if(cursor.moveToFirst()){
                do{
                    val account = cursor.getString(cursor.getColumnIndexOrThrow("account"))
                    if(account!=inputText)  continue
                    flag = 1
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val imageuri = cursor.getString(cursor.getColumnIndexOrThrow("imageuri"))
                    friendList.add(Friend(name,account,imageuri,email))
                    replaceFragment(RecycleViewFragment(friendList))
                    break
                }while(cursor.moveToNext())
                if(flag==0) replaceFragment(NoResultFragment())
            }
            else{
                replaceFragment(NoResultFragment())
            }
            cursor.close()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.search_result, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}