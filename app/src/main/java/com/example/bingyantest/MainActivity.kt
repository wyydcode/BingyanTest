package com.example.bingyantest

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.activity.BaseActivity
import com.example.bingyantest.activity.UserInformation
import com.example.bingyantest.adapters.FriendAdapter
import com.example.bingyantest.fragment.ContactsFragment
import com.example.bingyantest.fragment.InformFragment
import com.example.bingyantest.fragment.MainFragment
import com.example.bingyantest.fragment.NewFriendListFragment
import com.example.bingyantest.objects.MyObjects
import com.example.bingyantest.objects.title.MainTitleLayout
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView = findViewById<NavigationView>(R.id.navView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        //设计点击头像效果
        val title = findViewById<MainTitleLayout>(R.id.titlelayout)
        val titleHome = title.findViewById<CircleImageView>(R.id.iconImage)
        //val layoutManager = LinearLayoutManager(this)
        //val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //val fragmentLayout = findViewById<FrameLayout>(R.id.main_fragment)
        val bottombar = findViewById<LinearLayout>(R.id.bottombar)
        val bottombutton1 = bottombar.findViewById<ImageView>(R.id.button1)
        val bottombutton2 = bottombar.findViewById<ImageView>(R.id.button2)
        val bottombutton3 = bottombar.findViewById<ImageView>(R.id.button3)
        val bottombutton4 = bottombar.findViewById<ImageView>(R.id.button4)
        bottombutton1.setOnClickListener{
            replaceFragment(MainFragment())
        }
        bottombutton2.setOnClickListener{
            replaceFragment(ContactsFragment())
        }
        bottombutton3.setOnClickListener{
            replaceFragment(NewFriendListFragment())
        }
        bottombutton4.setOnClickListener{
            replaceFragment(InformFragment())
        }
        //recyclerView.layoutManager = layoutManager
        titleHome.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
            Toast.makeText(this,"userimage",Toast.LENGTH_SHORT).show()//触发滑动菜单
        }

        navView.setCheckedItem(R.id.navChange)
        navView.setNavigationItemSelectedListener {
            UserInformation.actionStart(this,MyObjects.userAccount)
            true
        }

        //refresh(recyclerView)
    }
    private fun refresh(recyclerView: RecyclerView){
        val adapter = FriendAdapter(MyObjects.friendslist.value!!)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

