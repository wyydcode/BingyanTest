package com.example.bingyantest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.adapters.FriendAdapter
import com.example.bingyantest.adapters.NewFriendAdapter
import com.example.bingyantest.objects.MyObjects

class NewFriendListFragment:Fragment(),MyObjects.DataUpdateListener {
    private var adapter: NewFriendAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.new_friend_list_fragment, container, false)
        MyObjects.registerDataUpdateListener(this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.new_friend_list_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = NewFriendAdapter(MyObjects.newFriendList)
        return view
    }

    override fun onDataUpdate() {
        adapter?.notifyDataSetChanged()
    }
}