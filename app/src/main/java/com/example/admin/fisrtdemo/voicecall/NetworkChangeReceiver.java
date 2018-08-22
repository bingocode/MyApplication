package com.example.admin.fisrtdemo.voicecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.event.NetWorkEvent;
import org.greenrobot.eventbus.EventBus;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * 创建时间: 2018/08/16 10:05 <br>
 * 作者: zengbin <br>
 * 描述:
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
  private static final String TAG = "NetworkChangeReceiver";
  @Override
  public void onReceive(Context context, Intent intent) {
    ConnectivityManager connectivity = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isNetWorkFine = false;
    if (connectivity != null) {
      NetworkInfo info = connectivity.getActiveNetworkInfo();
      if (info != null && info.isConnected()) {
        isNetWorkFine = true;
        if (info.getType() == TYPE_MOBILE) {
          EventBus.getDefault().post(new NetWorkEvent(context.getString(R.string.chatui_voice_call_network_mobile)));
        } else if (info.isAvailable()){
          EventBus.getDefault().post(new NetWorkEvent(""));
        } else {
          isNetWorkFine = false;
        }
      }
    }
    if (!isNetWorkFine) {
      EventBus.getDefault().post(new NetWorkEvent(context.getString(R.string.chatui_voice_call_net_problem_suggest)));
    }
  }
}


