package com.example.admin.fisrtdemo.model;

/**
 * Created by admin on 2017/7/9.
 */

public class Fruit {
    private  String name;
    private  int imageid;

    public Fruit(String name, int imageid) {
        this.name = name;
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public int getImageid() {
        return imageid;
    }
}
