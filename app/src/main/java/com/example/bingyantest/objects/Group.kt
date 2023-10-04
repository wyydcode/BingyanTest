package com.example.bingyantest.objects

data class Group(val name: String = "groupName", val member: ArrayList<Friend> = ArrayList<Friend>(),var isExpand: Boolean = true)