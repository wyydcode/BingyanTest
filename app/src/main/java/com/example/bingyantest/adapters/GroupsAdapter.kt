package com.example.bingyantest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.objects.Group

class GroupsAdapter (val groupList: ArrayList<Group>) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupItem: RecyclerView = view.findViewById(R.id.group_item)
        //val groupName: TextView = view.findViewById(R.id.group_name)
        init{
            groupItem.layoutManager = LinearLayoutManager(view.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val friend = groupList[position]
            Toast.makeText(
                parent.context, "you clicked view ${friend.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupList[position]
        val contactsAdapter = ContactsAdapter(group.member)
        holder.groupItem.adapter = contactsAdapter
        if(group.isExpand){
            holder.groupItem.visibility = View.VISIBLE
        }else{
            holder.groupItem.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            group.isExpand = !group.isExpand
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = groupList.size
}