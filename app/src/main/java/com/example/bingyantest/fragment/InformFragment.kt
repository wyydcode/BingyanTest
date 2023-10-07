package com.example.bingyantest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingyantest.R
import com.example.bingyantest.adapters.InformationAdapter
import com.example.bingyantest.adapters.NewFriendAdapter
import com.example.bingyantest.objects.MyObjects

class InformFragment : Fragment() {
    val informList = MyObjects.informList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_information, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.information_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = InformationAdapter(informList)
        return view
    }
}