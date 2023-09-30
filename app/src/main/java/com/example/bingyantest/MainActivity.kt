package com.example.bingyantest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.adapters.FriendAdapter
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.title.MainTitleLayout
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private val friendList = ArrayList<Friend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView = findViewById<NavigationView>(R.id.navView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        //设计点击头像效果
        val title = findViewById<MainTitleLayout>(R.id.titlelayout)
        val titleHome = title.findViewById<CircleImageView>(R.id.iconImage)
        titleHome.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
            Toast.makeText(this,"userimage",Toast.LENGTH_SHORT).show()//触发滑动菜单
        }

        navView.setCheckedItem(R.id.navCall)
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }
        val dbHelper = MyDatabaseHelper(this,"Friends.db",1)
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = FriendAdapter(friendList)
        recyclerView.adapter = adapter

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.backup -> Toast.makeText(this, "You clicked Backup",
                Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "You clicked Delete",
                Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "You clicked Settings",
                Toast.LENGTH_SHORT).show()
        }
        return true
    }
}

