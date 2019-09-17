package com.example.admin.fisrtdemo.voicecall;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.guide.guideview.ChatGuide;

public class VoipActivity extends AppCompatActivity {
  private static final String TAG = "VoipActivity";
  private Button mFloatButon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voip);
    mFloatButon = findViewById(R.id.showFloat);
    mFloatButon.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        moveTaskToBack(true);
      }
    });
    findViewById(R.id.g1).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new ChatGuide.Builder(VoipActivity.this).label("g1")
            .color(getResources().getColor(R.color.chatui_voice_call_small_window_text))
            .highLight(mFloatButon)
            .build()
            .show();
      }
    });

    findViewById(R.id.g2).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });

    findViewById(R.id.g3).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });

    findViewById(R.id.g4).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new ChatGuide.Builder(VoipActivity.this).label("g4")
            .color(getResources().getColor(R.color.chatui_voice_call_small_window_text))
            .build()
            .show();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.i(TAG, "onStart");
    FloatWindowManager.hideFloatWindow();
  }

  @Override protected void onPause() {
    Log.i(TAG, "onPause");
    super.onPause();
  }

  @Override
  protected void onStop() {
    Log.i(TAG, "onStop");
    if (Build.VERSION.SDK_INT >= 23) {
      Log.i(TAG, "sdk >= 23");
      if (Settings.canDrawOverlays(this)) {
        Log.i(TAG, "has Permission");
        FloatWindowManager.showFloatWindow(this, R.layout.chatui_item_tel_small);
      } else {
        Log.i(TAG, "no Permmission");
      }
    } else {
      Log.i(TAG, "sdk < 23");
      FloatWindowManager.showFloatWindow(this, R.layout.chatui_item_tel_small);
    }
    super.onStop();
  }

  @Override protected void onDestroy() {
    Log.i(TAG, "onDestory");
    //FloatWindowManager.hideFloatWindow();
    super.onDestroy();
  }
}
