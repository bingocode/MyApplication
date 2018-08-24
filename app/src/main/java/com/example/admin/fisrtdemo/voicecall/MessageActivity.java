package com.example.admin.fisrtdemo.voicecall;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.admin.fisrtdemo.R;

/**
 * 锁屏提示Activity
 */
public class MessageActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i("tag", "onCreate:启动了消息内容的activity ");
    //四个标志位顾名思义，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕。这样当Activity启动的时候，它会解锁并亮屏显示。
    Window win = getWindow();
    win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
    Drawable wallPaper = WallpaperManager.getInstance( this).getDrawable();
    win.setBackgroundDrawable(wallPaper);
    setContentView(R.layout.activity_message);
    initView();
  }

  private void initView() {
    findViewById(R.id.message_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //先解锁系统自带锁屏服务，放在锁屏界面里面
        KeyguardManager
            keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.newKeyguardLock("").disableKeyguard(); //解锁
        //点击进入消息对应的页面
        startActivity(new Intent(MessageActivity.this, VoiceCallActivity.class));
        finish();
      }
    });

    findViewById(R.id.close_iv).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}
