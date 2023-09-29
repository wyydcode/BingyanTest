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

class ChatActivity : AppCompatActivity() {
    private val msgList = ArrayList<Msg>()
    private var adapter: MsgAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
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
                val msg = Msg(content, Msg.TYPE_SENT)
                msgList.add(msg)
                adapter?.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
                recyclerView.scrollToPosition(msgList.size - 1) // 将RecyclerView定位到最后一行
                inputText.setText("") // 清空输入框中的内容

            }
        }
    }

    private fun initMsg() {
        val msg1 = Msg("Hello guy.", Msg.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = Msg("Hello. Who is that?", Msg.TYPE_SENT)
        msgList.add(msg2)
        val msg3 = Msg("This is Tom. Nice talking to you. ", Msg.TYPE_RECEIVED)
        msgList.add(msg3)
    }

    companion object {
        fun actionStart(context: Context, data1: String, data2: String, data3: String, data4: String) {
            val intent = Intent(context, FriendInformation::class.java)
            intent.putExtra("data1", data1)
            intent.putExtra("data2", data2)
            intent.putExtra("data3", data3)
            intent.putExtra("data4", data4)
            context.startActivity(intent)
        }
    }
}
