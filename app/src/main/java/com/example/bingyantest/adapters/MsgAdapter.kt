package com.example.bingyantest.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.activity.FriendInformation
import com.example.bingyantest.objects.Msg
import com.example.bingyantest.objects.MyObjects
import de.hdodenhof.circleimageview.CircleImageView


class MsgAdapter(val msgList: List<Msg>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftMsg: TextView = view.findViewById(R.id.leftMsg)
    }
    inner class RightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rightMsg: TextView = view.findViewById(R.id.rightMsg)
    }
    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType ==
        Msg.TYPE_RECEIVED) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_left_item,
            parent, false)
        LeftViewHolder(view)
    } else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_right_item,
            parent, false)
        RightViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        val friendaccount = msg.sender
        val friend = MyObjects.query(friendaccount)
        when (holder) {
            is LeftViewHolder -> {
                holder.leftMsg.text = msg.content
                val icon = holder.itemView.findViewById<CircleImageView>(R.id.iconImage)
                icon.setImageURI(Uri.parse(friend.imageuri))
                icon.setOnClickListener{
                    FriendInformation.actionStart(holder.itemView.context,friend.name,friend.account,friend.email,friend.imageuri)
                }
            }
            is RightViewHolder -> {
                val icon = holder.itemView.findViewById<CircleImageView>(R.id.iconImage)
                icon.setImageURI(Uri.parse(MyObjects.user.imageuri))
                holder.rightMsg.text = msg.content
            }
            else -> throw IllegalArgumentException()
        }
    }
    override fun getItemCount() = msgList.size
}