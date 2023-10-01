package com.example.bingyantest.activity

import android.content.Context
import android.content.Intent
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
    private var adapter: MsgAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        friendaccount= intent.getStringExtra("name")!!
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

            }
        }
    }

    private fun initMsg() {
        val msg1 = Msg(MyObjects.userAccount,friendaccount,"Hello guy.",LocalDateTime.now().toString(), Msg.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = Msg(MyObjects.userAccount,friendaccount,"Hello. Who is that?",LocalDateTime.now().toString(), Msg.TYPE_SENT)
        msgList.add(msg2)
        val msg3 = Msg(MyObjects.userAccount,friendaccount,"This is Tom. Nice talking to you. ",LocalDateTime.now().toString(), Msg.TYPE_RECEIVED)
        msgList.add(msg3)
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
