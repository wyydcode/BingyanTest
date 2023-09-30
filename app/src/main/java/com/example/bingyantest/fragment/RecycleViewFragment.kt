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
import com.example.bingyantest.objects.Friend


class RecycleViewFragment(friendList:ArrayList<Friend>):Fragment() {
    val friendList = friendList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(com.example.bingyantest.R.layout.recycleview_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewfragment)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = FriendAdapter(friendList)
        return view
    }

}