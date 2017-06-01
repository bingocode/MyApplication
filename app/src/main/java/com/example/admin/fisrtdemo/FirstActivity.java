package com.example.admin.fisrtdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,FirstActivity.class);
        context.startActivity(intent);
    }
}
