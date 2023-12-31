package com.example.bingyantest.adapters

import android.content.ContentValues
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.Group
import com.example.bingyantest.objects.MyObjects

class GroupListAdapter(val groupList: ArrayList<Group>,val friend: Friend) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    var lastClickTime = 0L
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.group_name_in_list)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grouplist_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if(currentTime - lastClickTime>= 1000000){
                val position = viewHolder.adapterPosition
                val group = groupList[position]

                MyObjects.groupList[position].member.add(friend)// 添加成员
                val db = MyObjects.dbHelper.writableDatabase//更新数据库
                val values = ContentValues().apply {
                    put("groups",group.name)
                }
                val cursor = db.query("Friends", arrayOf("groups"),"account = ?", arrayOf("${friend.account}"),null,null,null)
                cursor.moveToFirst()
                val friendGroup =  cursor.getString(cursor.getColumnIndexOrThrow("groups"))
                db.update("Friends",values,"account = ?", arrayOf("${friend.account}"))
                MyObjects.groupList.forEach{
                    if(it.name==friendGroup){
                        it.member.remove(friend)
                    }
                }
                lastClickTime = currentTime
                Toast.makeText(parent.context,"设置分组成功",Toast.LENGTH_SHORT).show()
            }
        }
        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupList[position]
        holder.groupName.text = group.name
    }
    override fun getItemCount() = groupList.size
}