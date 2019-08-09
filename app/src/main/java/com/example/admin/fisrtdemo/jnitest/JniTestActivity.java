package com.example.admin.fisrtdemo.jnitest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.admin.fisrtdemo.R;
import com.whu.zengbin.mutiview.util.LogUtil;

public class JniTestActivity extends AppCompatActivity {
  private static final String TAG = "JniTestActivity";
  private TextView mShowTv;
  private Button mGetBtn;
  private Button mGetBtn2;
  private Button mGetBtn3;
  private Button mGetBtn4;
  private Button mGetBtn5;
  private Button mGetBtn6;
  private int a = 9;
  private int b = 3;

  public static void start(Context context) {
    context.startActivity(new Intent(context, JniTestActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_jni_test);
    mShowTv = findViewById(R.id.showTv);
    mGetBtn = findViewById(R.id.getBtn);
    mGetBtn2 = findViewById(R.id.getBtn2);
    mGetBtn3 = findViewById(R.id.getBtn3);
    mGetBtn4 = findViewById(R.id.getBtn4);
    mGetBtn5 = findViewById(R.id.getBtn5);
    mGetBtn6 = findViewById(R.id.getBtn6);
    mGetBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String str = NDKTools.getStringFromNDK();
        LogUtil.i(TAG, "get Native String = " + str);
        mShowTv.setText(str);
      }
    });

    mGetBtn2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        JniDemo1 demo1 = new JniDemo1();
        String str = demo1.sayHello(2L);
        LogUtil.i(TAG, "get Native dynamic String = " + str);
        mShowTv.setText(str);
      }
    });
    mGetBtn3.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        int rst = JNIToola.add(a, b);
        LogUtil.i(TAG, "add result = " + rst);
        mShowTv.setText(String.valueOf(rst));
      }
    });
    mGetBtn4.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        int rst = JNIToola.sub(a, b);
        LogUtil.i(TAG, "sub result = " + rst);
        mShowTv.setText(String.valueOf(rst));
      }
    });
    mGetBtn5.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        int rst = JNIToola.mul(a, b);
        LogUtil.i(TAG, "mul result = " + rst);
        mShowTv.setText(String.valueOf(rst));
      }
    });
    mGetBtn6.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        int rst = JNIToola.div(a, b);
        LogUtil.i(TAG, "div result = " + rst);
        mShowTv.setText(String.valueOf(rst));
      }
    });
  }
}
