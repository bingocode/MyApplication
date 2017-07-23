package com.example.admin.fisrtdemo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by admin on 2017/6/1.
 */

public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);

        Button back=(Button)findViewById(R.id.back);
        Button more=(Button)findViewById(R.id.more);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c=getContext();
                Toast.makeText(c, "back", Toast.LENGTH_SHORT).show();
                ((Activity)c).finish();
            }
        });

        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c=getContext();
                Toast.makeText(c, "more", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
