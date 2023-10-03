package com.example.bingyantest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.activity.UserInformation
import com.example.bingyantest.adapters.FriendAdapter
import com.example.bingyantest.datasave.MyDatabaseHelper
import com.example.bingyantest.fragment.ContactsFragment
import com.example.bingyantest.fragment.MainFragment
import com.example.bingyantest.fragment.NewFriendListFragment
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.MyObjects
import com.example.bingyantest.objects.title.MainTitleLayout
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(),MyObjects.DataUpdateListener{
    private var friendList = ArrayList<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        MyObjects.registerDataUpdateListener(this)
        val navView = findViewById<NavigationView>(R.id.navView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        //设计点击头像效果
        val title = findViewById<MainTitleLayout>(R.id.titlelayout)
        val titleHome = title.findViewById<CircleImageView>(R.id.iconImage)
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //val fragmentLayout = findViewById<FrameLayout>(R.id.main_fragment)
        val bottombar = findViewById<LinearLayout>(R.id.bottombar)
        val bottombutton1 = bottombar.findViewById<Button>(R.id.button1)
        val bottombutton2 = bottombar.findViewById<Button>(R.id.button2)
        val bottombutton3 = bottombar.findViewById<Button>(R.id.button3)
        bottombutton1.setOnClickListener{
            replaceFragment(MainFragment())
        }
        bottombutton2.setOnClickListener{
            replaceFragment(ContactsFragment())
        }
        bottombutton3.setOnClickListener{
            replaceFragment(NewFriendListFragment())
        }
        recyclerView.layoutManager = layoutManager
        titleHome.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
            Toast.makeText(this,"userimage",Toast.LENGTH_SHORT).show()//触发滑动菜单
        }

        navView.setCheckedItem(R.id.navChange)
        navView.setNavigationItemSelectedListener {
            UserInformation.actionStart(this,MyObjects.userAccount)
            true
        }

        refresh(recyclerView)
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
    private fun refresh(recyclerView: RecyclerView){
        val adapter = FriendAdapter(friendList)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
    inner class AgreeBroadcast: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            friendList = MyObjects.friendslist.value!!
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            refresh(recyclerView)
        }
    }

    override fun onDataUpdate() {
        friendList = MyObjects.friendslist.value!!
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        refresh(recyclerView)
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

