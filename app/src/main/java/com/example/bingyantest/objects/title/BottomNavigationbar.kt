package com.example.bingyantest.objects.title

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.bingyantest.R

class BottomNavigationbar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_navigationbar, this)
    }
}