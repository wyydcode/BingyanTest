package com.example.bingyantest.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.adapters.AddFriendAdapter
import com.example.bingyantest.objects.Friend
import com.example.bingyantest.objects.MyObjects


class RecycleViewFragment(val friendList:ArrayList<Friend>):Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.recyclerview_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_fragment)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AddFriendAdapter(friendList)
        return view
    }

}