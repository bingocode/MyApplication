package com.example.admin.fisrtdemo.voicecall;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.admin.fisrtdemo.R;

public class VoipActivity extends AppCompatActivity {
  private static final String TAG = "VoipActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voip);
    Button button = findViewById(R.id.showFloat);
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        moveTaskToBack(true);
      }
    });
  }

  @Override protected void onStart() {
    super.onStart();
    Log.i(TAG, "onStart");
    FloatWindowManager.hideFloatWindow();
  }

  @Override protected void onPause() {
    Log.i(TAG, "onPause");
    super.onPause();
  }

  @Override protected void onStop() {
    Log.i(TAG, "onStop");
    if (Build.VERSION.SDK_INT >= 23 ) {
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
