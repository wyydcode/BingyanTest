package com.example.bingyantest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.adapters.GroupsAdapter
import com.example.bingyantest.objects.MyObjects

class ContactsFragment : Fragment() {
    val groupList = MyObjects.groupList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.group_member_item, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.group_list_member_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = GroupsAdapter(groupList)
        return view
    }
}
