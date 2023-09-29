package com.example.bingyantest.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.activity.ChatActivity
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.title.ChatTitleLayout

class FriendsAdapter(val friendList: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friendImage: ImageView = view.findViewById(R.id.friendImage)
        val friendName: TextView = view.findViewById(R.id.friendName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val friend = friendList[position]
            Toast.makeText(parent.context, "you clicked view ${friend.name}",
                Toast.LENGTH_SHORT).show()
            ChatActivity.actionStart(parent.context,friend.name,friend.account,friend.email,friend.imageuri)
        }
        viewHolder.friendImage.setOnClickListener {
            val position = viewHolder.adapterPosition
            val friend = friendList[position]
            Toast.makeText(parent.context, "you clicked image ${friend.name}",
                Toast.LENGTH_SHORT).show()
            ChatActivity.actionStart(parent.context,friend.name,friend.account,friend.email,friend.imageuri)
        }
        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friends = friendList[position]
        val uri = Uri.parse(friends.imageuri)
        holder.friendImage.setImageURI(uri)
        holder.friendName.text = friends.name
    }
    override fun getItemCount() = friendList.size
}