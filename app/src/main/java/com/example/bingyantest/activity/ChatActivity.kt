package com.example.bingyantest.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.adapters.MsgAdapter
import com.example.bingyantest.objects.Msg
import com.example.bingyantest.objects.MyObjects
import java.time.LocalDateTime

class ChatActivity : AppCompatActivity() {
    private val msgList = ArrayList<Msg>()
    private lateinit var friendaccount:String
    private val userAccount = MyObjects.userAccount
    private var adapter: MsgAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        friendaccount= intent.getStringExtra("account")!!
        initMsg()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = MsgAdapter(msgList)
        recyclerView.adapter = adapter
        val send = findViewById<Button>(R.id.send)
        val inputText = findViewById<EditText>(R.id.inputText)
        send.setOnClickListener {
            val content = inputText.text.toString()
            if (content.isNotEmpty()) {
                val msg = Msg(MyObjects.userAccount,friendaccount,content,LocalDateTime.now().toString(), Msg.TYPE_SENT)
                msgList.add(msg)
                adapter?.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
                recyclerView.scrollToPosition(msgList.size - 1) // 将RecyclerView定位到最后一行
                inputText.setText("") // 清空输入框中的内容
                val db = MyObjects.dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("sender",userAccount)
                    put("receiver",friendaccount)
                    put("content",content)
                    put("date",LocalDateTime.now().toString())
                }
                db.insertWithOnConflict("Chats",null,values,SQLiteDatabase.CONFLICT_IGNORE)
            }
        }
    }

    private fun initMsg() {
        val db = MyObjects.dbHelper.writableDatabase
        val query = "SELECT * FROM chats WHERE (sender = $userAccount AND receiver = $friendaccount) OR (sender = $friendaccount AND receiver = $userAccount)"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                // 处理查询结果
                val sender = cursor.getString(cursor.getColumnIndexOrThrow("sender"))
                val receiver = cursor.getString(cursor.getColumnIndexOrThrow("receiver"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                // 在这里进行处理，例如将结果添加到列表中
                val type = if(receiver==friendaccount){
                    Msg.TYPE_SENT
                }else{
                    Msg.TYPE_RECEIVED
                }
                val msg = Msg(sender,receiver,content,date,type)
                msgList.add(msg)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    companion object {
        fun actionStart(context: Context, name: String, account: String, email: String, imageuri: String) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("account", account)
            intent.putExtra("email", email)
            intent.putExtra("imageuri", imageuri)
            context.startActivity(intent)
        }
    }
}
