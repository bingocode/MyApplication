package com.example.admin.fisrtdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_custom_view.*

class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        customview.setOnClickListener { Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show() }
        startscroll.setOnClickListener { layout.startScroll() }
    }
    companion object{
        const val TAG = "CustomTag"
    }
}
