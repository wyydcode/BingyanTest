package com.example.bingyantest.objects.title

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.bingyantest.R
import com.example.bingyantest.activity.AddFriend
import com.example.bingyantest.objects.MyObjects
import de.hdodenhof.circleimageview.CircleImageView

class MainTitleLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_main, this)
        val titleAdd = findViewById<Button>(R.id.add)//后续修改为添加好友菜单
        val icon = findViewById<CircleImageView>(R.id.iconImage)
        icon.setImageURI(Uri.parse(MyObjects.user.imageuri))
        titleAdd.setOnClickListener {
            Toast.makeText(context, "You clicked Edit button", Toast.LENGTH_SHORT).show()
            val intent = Intent(context,AddFriend::class.java)
            startActivity(context,intent,null)
        }
    }
}