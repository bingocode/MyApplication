package com.example.admin.fisrtdemo.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.admin.fisrtdemo.R
import com.example.admin.fisrtdemo.model.Item
import kotlinx.android.synthetic.main.view_item.view.*

/**
 * Created by admin on 2017/7/16.
 */

class  ItemAdapter(val items: List<Item>) :RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent?.context,R.layout.view_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            item_title.text = items[position].title
//            setOnClickListener { listener(items[position]) }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
