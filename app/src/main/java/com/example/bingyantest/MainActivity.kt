package com.example.bingyantest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.adapters.FriendsAdapter
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.objects.Friend
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val friendList = ArrayList<Friend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView = findViewById<NavigationView>(R.id.navView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navView.setCheckedItem(R.id.navCall)
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }
        val dbHelper = MyDatabaseHelper(this,"Friends.db",1)
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = FriendsAdapter(friendList)
        recyclerView.adapter = adapter

    }

}

