package com.example.admin.fisrtdemo;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.whu.zengbin.mutiview.util.LogUtil;

public class TalkActivity extends AppCompatActivity {
  private static final String TAG = "TalkActivity";
  ImageView mAudioIcon;
  ImageView mSmall;
  int animResId = R.drawable.chatui_icon_voice_right ;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_talk);
    mAudioIcon = (ImageView) findViewById(R.id.iv_icon_voice);
    mSmall = (ImageView) findViewById(R.id.small);
    mAudioIcon.setBackgroundResource(animResId);
    AnimationDrawable audioIconAnim =
        (AnimationDrawable) mAudioIcon.getBackground();
    audioIconAnim.start();
    //
    //mSmall.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    FloatWindowManager.showFloatWindow(TalkActivity.this,R.layout.item_tel_small);
    //    moveTaskToBack(true);
    //  }
    //});
  }

  @Override protected void onStart() {
    super.onStart();
    LogUtil.i(TAG, "Onstart");
    //if (FloatWindowManager.isShown) {
    //  FloatWindowManager.hideFloatWindow();
    //}
  }

  @Override protected void onResume() {
    super.onResume();
    LogUtil.i(TAG, "onResume");

  }
  @Override protected void onPause() {
    super.onPause();
    LogUtil.i(TAG, "onPause");

  }

  @Override protected void onStop() {
    super.onStop();
    LogUtil.i(TAG, "onStop");

  }

  @Override protected void onDestroy() {
    super.onDestroy();
    LogUtil.i(TAG, "onDestroy");

  }
}
