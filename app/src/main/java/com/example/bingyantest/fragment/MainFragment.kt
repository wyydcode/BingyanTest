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
import com.example.bingyantest.objects.MyObjects

class MainFragment :Fragment(),MyObjects.DataUpdateListener{
    private var adapter:FriendAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MyObjects.registerDataUpdateListener(this)
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = FriendAdapter(MyObjects.friendslist.value!!)
        return view
    }

    override fun onDataUpdate() {
        adapter?.notifyDataSetChanged()
        //adapter?.notifyItemInserted(MyObjects.friendslist.value!!.size-1)
    }
}