package com.example.admin.fisrtdemo

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.example.admin.fisrtdemo.adapter.ItemAdapter
import com.example.admin.fisrtdemo.model.getItems
import kotlinx.android.synthetic.main.activity_my_listview.*

class MyListviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_listview)
        myrecycler.layoutManager=GridLayoutManager(this,2)
        myrecycler.adapter=ItemAdapter(getItems())
        val message : String = "hello"
        Toast.makeText(this,"[$localClassName] $message",Toast.LENGTH_SHORT)
    }
    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context,MyListviewActivity::class.java))
        }
    }

}
