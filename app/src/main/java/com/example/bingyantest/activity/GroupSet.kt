package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.adapters.GroupListAdapter
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.Group
import com.example.bingyantest.objects.MyObjects

class GroupSet:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_set)
        val button = findViewById<Button>(R.id.new_group)
        val editText = findViewById<EditText>(R.id.new_group_name)
        val friend = Friend(intent.getStringExtra("name")!!,intent.getStringExtra("account")!!,intent.getStringExtra("email")!!,intent.getStringExtra("imageuri")!!)
        button.setOnClickListener {
            val text = editText.text.toString()
            if(text!=null&&text!=""){
                MyObjects.groupList.add(Group(text, ArrayList<Friend>()))
                MyObjects.groupList[MyObjects.groupList.size-1].member.add(friend)//添加
                val db = MyObjects.dbHelper.writableDatabase//更新数据库
                val values = ContentValues().apply {
                    put("groups",text)
                }
                db.update("Friends",values,"account = ?", arrayOf("${friend.account}"))//存入数据库
                Toast.makeText(this,"成功建立分组",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"请输入分组名",Toast.LENGTH_SHORT).show()
            }
        }
        val recyclerView = findViewById<RecyclerView>(R.id.group_set_recyclerview)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = GroupListAdapter(MyObjects.groupList,friend)
        recyclerView.adapter = adapter
    }
    companion object {
        fun actionStart(context: Context, name: String, account: String, email: String, imageuri: String) {
            val intent = Intent(context, GroupSet::class.java)
            intent.putExtra("name", name)
            intent.putExtra("account", account)
            intent.putExtra("email", email)
            intent.putExtra("imageuri", imageuri)
            context.startActivity(intent)
        }
    }
}