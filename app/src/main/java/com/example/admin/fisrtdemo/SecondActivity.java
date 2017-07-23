package com.example.admin.fisrtdemo;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.fisrtdemo.adapter.QuickFragmentPagerAdapter;
import com.example.admin.fisrtdemo.adapter.QuickViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends BaseActivity {
    TextView text;
    ViewPager viewPager;
    List<Fragment> views;
    List<String> titlelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        text=(TextView)findViewById(R.id.text);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        initData();
        initView();
    }
    private void initData(){
        titlelist=new ArrayList<String>();
        titlelist.add("第一");
        titlelist.add("第二");
        titlelist.add("第三");


        views=new ArrayList<Fragment>();
        views.add(new Fragment1());
        views.add(new Fragment2());
        views.add(new Fragment3());

    }
    private void initView(){
        QuickFragmentPagerAdapter mypageradapter=new QuickFragmentPagerAdapter<Fragment>(getSupportFragmentManager(),views,titlelist);
        viewPager.setAdapter(mypageradapter);
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,SecondActivity.class);
        context.startActivity(intent);
    }
}
