package com.example.admin.fisrtdemo.voicecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 创建时间: 2018/08/23 20:29 <br>
 * 作者: zengbin <br>
 * 描述:
 */
public class HomeListener {
  static final String TAG = "HomeListener";
  private Context mContext;
  private IntentFilter mFilter;
  private OnHomePressedListener mListener;
  private InnerRecevier mRecevier;

  // 回调接口
  public interface OnHomePressedListener {
    //home回调
    void onHomePressed();

    //任务回调
    void onHomeLongPressed();
  }

  public HomeListener(Context context) {
    mContext = context;
    //找到intent的标识ACTION_CLOSE_SYSTEM_DIALOGS指示意思为：
    //广播行为：当用户操作请求临时退出当前对话框时发送广播
    mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
  }

  /**
   * 设置监听
   */
  public void setOnHomePressedListener(OnHomePressedListener listener) {
    mListener = listener;
    mRecevier = new InnerRecevier();
  }

  /**
   * 开始监听，注册广播
   */
  public void startWatch() {
    if (mRecevier != null) {
      mContext.registerReceiver(mRecevier, mFilter);
    }
  }

  /**
   * 停止监听，注销广播
   */
  public void stopWatch() {
    if (mRecevier != null) {
      mContext.unregisterReceiver(mRecevier);
    }
  }

  /**
   * 广播接收者
   */
  class InnerRecevier extends BroadcastReceiver {
    final String SYSTEM_DIALOG_REASON_KEY = "reason";
    final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
        String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
        if (reason != null) {
          //                    Log.e(TAG, "action:" + action + ",reason:" + reason);
          if (mListener != null) {
            if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
              // home键
              mListener.onHomePressed();
            } else if (reason
                .equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
              // 任务键
              mListener.onHomeLongPressed();
            }
          }
        }
      }
    }
  }
}