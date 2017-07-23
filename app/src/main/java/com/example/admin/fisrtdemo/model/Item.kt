package com.example.admin.fisrtdemo.model

/**
 * Created by admin on 2017/7/16.
 */
fun getItems():List<Item> {
    return (1..10).map { Item(it.toString()) }
}
data  class  Item(val title: String)
