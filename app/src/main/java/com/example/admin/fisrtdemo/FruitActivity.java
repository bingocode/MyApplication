package com.example.admin.fisrtdemo;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class FruitActivity extends AppCompatActivity {
    public static  final String FRUIT_NAME="fruit_name";
    public static  final String FRUIT_IMG_ID="fruit_img_id";
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView fruitImageView;
    TextView fruitcontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        Intent intent=getIntent();
        String fruitName=intent.getStringExtra(FRUIT_NAME);
        int fruitImgid=intent.getIntExtra(FRUIT_IMG_ID,0);


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        fruitImageView=(ImageView)findViewById(R.id.fruit_imageview);
        fruitcontent=(TextView)findViewById(R.id.fruit_content);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(fruitName);
        Glide.with(this).load(fruitImgid).into(fruitImageView);
        String context=generateContext(fruitName);
        fruitcontent.setText(context);
    }
    private String generateContext(String s){
        StringBuilder fc=new StringBuilder();
        for(int i=0;i<100;i++){
            fc.append(s);
        }
        return  fc.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
