package com.example.admin.fisrtdemo.voicecall;

/**
 * 创建时间: 2018/08/18 15:35 <br>
 * 作者: zengbin <br>
 * 描述: 监听普通电话
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

public class TelPhoneReceiver extends BroadcastReceiver {
  public static boolean isbusy = false;
  private static final String TAG = "TelPhoneReceiver";

  private TelephonyManager tm;

  private MyListener listener;

  private PhoneStateCallBack mCallback;

  public TelPhoneReceiver(Context context,PhoneStateCallBack callback) {
    tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
    mCallback = callback;
    listener = new MyListener();
  }

  public TelPhoneReceiver() {}

  public void start() {
    Log.i(TAG, "TelPhoneReceiver");
    tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
  }

  public void destory() {
    tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    listener = null;
    tm = null;
    isbusy = false;
    mCallback = null;
  }

  @Override public void onReceive(Context context, Intent intent) {
    Log.i(TAG,"action"+intent.getAction());

    //如果是去电
    if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
      String phoneNumber = intent
          .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
      Log.d(TAG, "call OUT:" + phoneNumber);
    } else {
      start();
    }
    }

  class MyListener extends PhoneStateListener {
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
      super.onCallStateChanged(state, incomingNumber);
      try {
        switch (state) {
          case TelephonyManager.CALL_STATE_IDLE://空闲状态。
            isbusy = false;
            Log.d(TAG, "CALL_STATE_IDLE:" );
            if (mCallback != null) {
              mCallback.onIdle();
            }
            break;
          case TelephonyManager.CALL_STATE_RINGING://零响状态。
            isbusy = true;
            Log.d(TAG, "CALL_STATE_RINGING:" );
            if (mCallback != null) {
              mCallback.onRing();
            }
            break;
          case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
            isbusy = true;
            Log.d(TAG, "CALL_STATE_OFFHOOK:" );
            if (mCallback != null) {
              mCallback.onOffhook();
            }
            break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  interface PhoneStateCallBack {
    void onIdle();
    void onRing();
    void onOffhook();
  }
}



