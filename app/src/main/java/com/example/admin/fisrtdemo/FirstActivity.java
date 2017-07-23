package com.example.admin.fisrtdemo;


import android.animation.Keyframe;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.fisrtdemo.adapter.FruitAdapter;
import com.example.admin.fisrtdemo.model.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FirstActivity extends BaseActivity {
    private Button submit;
    private EditText editText;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private FloatingActionButton floatbtn;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private Fruit[] fruits={
            new Fruit("Apple",R.drawable.delete),
            new Fruit("Banana",R.drawable.backup),
            new Fruit("Pear",R.drawable.setting)
    };

    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
         toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navView=(NavigationView)findViewById(R.id.nav_view);
        floatbtn=(FloatingActionButton)findViewById(R.id.floatbtn);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);


        initData();
        initView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);//与xml里定义的layout_gravity要一直
                break;
            case R.id.backup:
                Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();break;
            case R.id.delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();break;
            case R.id.settings:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();break;
            default:
        }
        return true;
    }

    private void initData(){
        initFruits();
    }
    private void initFruits(){
        fruitList.clear();
        for(int i=0;i<30;i++){
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }

    }

    private void initView(){
//        submit=(Button)findViewById(R.id.submit);
//        editText=(EditText)findViewById(R.id.edittext);
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent("com.example.admin.fisrtdemo.ACTION_START");
//                intent.addCategory("com.example.admin.fisrtdemo.MY_CATAGORY");
//                intent.putExtra("data",editText.getText().toString());
//                startActivity(intent);
//            }
//        });
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);


        actionBar=getSupportActionBar();//设置toolbar时必须获取的入口
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//显示Toolbar最左侧的原生按钮，默认图标时箭头
            actionBar.setHomeAsUpIndicator(R.drawable.nav);//修改成自己的图标？有问题
        }

        navView.setCheckedItem(R.id.nav_call);//设置其中一个菜单默认被选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"data deleted",Snackbar.LENGTH_SHORT).setAction("undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FirstActivity.this, "stored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

    }
    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,FirstActivity.class);
        context.startActivity(intent);
    }
}
