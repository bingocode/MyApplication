package com.example.admin.fisrtdemo.voicecall;

/**
 * 创建时间: 2018/08/14 17:28 <br>
 * 作者: zengbin <br>
 * 描述:声音控制器(听筒切换/插拔耳机监听)
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

public class CallAudioController implements SensorEventListener {
  private final static String TAG = "CallAudioController";

  private final static String BRAND_SAMSUNG = "samsung";

  private AudioManager mAudioManager = null; // 声音管理器
  private SensorManager mSensorManager = null; // 传感器管理器
  private Sensor mProximiny = null; // 传感器实例

  private PowerManager mLocalPowerManager = null;//电源管理对象
  private PowerManager.WakeLock mLocalWakeLock = null;//电源锁
  private boolean isAudioPlaying = false;
  private boolean isWiredHeadsetOn = false;
  protected BroadcastReceiver mHeadsetReceiver;

  //部分手机如(Samsung SM-N9200)导致的ChatFragment生命周期调用,不执行具体操作
  private boolean isCasedByWakeLock = false;

  private Activity mContext;

  public CallAudioController(Activity context) {
    init(context);
  }

  private void init(Activity context) {
    this.mContext = context;
    mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    mProximiny = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    mLocalPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
  }

  /**
   * 注册耳机广播,传感器
   */
  public void register() {
    mHeadsetReceiver = new HeadsetReceiver();
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
    intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
    mContext.registerReceiver(mHeadsetReceiver, intentFilter);

    if (mSensorManager != null) {
      mSensorManager.registerListener(this, mProximiny, SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  /**
   * 解除耳机广播,传感器等注册
   */
  public void unRegister() {
    mContext.unregisterReceiver(mHeadsetReceiver);
    if (mSensorManager != null) {
      mSensorManager.unregisterListener(this);
    }
    setScreenOn();
  }

  /**
   * 设置播放状态
   */
  public void setAudioPlaying(boolean audioPlaying) {
    isAudioPlaying = audioPlaying;
  }

  /**
   * 销毁
   */
  public void destroy() {
    if (mSensorManager != null) {
      if (mLocalWakeLock != null) {
        mLocalWakeLock.release();
      }
      mLocalWakeLock = null;
    }
  }

  private synchronized void setSpeakerphoneOn(boolean on) {
    Log.i(TAG, "setSpeakerphoneOn:" + on);
    mAudioManager.setSpeakerphoneOn(on);
    if (on) {
      mAudioManager.setMode(AudioManager.MODE_NORMAL);
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.setStreamVolume(AudioManager.MODE_IN_COMMUNICATION,
            mAudioManager.getStreamMaxVolume(AudioManager.MODE_IN_COMMUNICATION),
            AudioManager.FX_KEY_CLICK);
      } else {
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
            mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
            AudioManager.FX_KEY_CLICK);
      }
    }
  }

  @Override public void onSensorChanged(SensorEvent event) {
    Log.i(TAG, "isWiredHeadsetOn:" + isWiredHeadsetOn + "; isPlaying:" + isAudioPlaying);
    float[] its = event.values;
    if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
      //Log.i(TAG, "onSensorChanged:" + its[0]);
      if (!isWiredHeadsetOn && isAudioPlaying) {
        //当手贴近距离感应器的时候its[0]返回值为0.0
        if (its[0] == 0.0) {// 贴近手机
          if (mLocalWakeLock == null) {
            setSpeakerphoneOn(false);
            setScreenOff();

          }
        } else {// 远离手机
          setScreenOn();
          setSpeakerphoneOn(true);
        }
      } else {
        if (its[0] != 0.0) {
          setScreenOn();
          setSpeakerphoneOn(true);
        }
      }
    }
  }

  private void setScreenOff() {
    isCasedByWakeLock = true;
    if (mLocalWakeLock == null) {
      mLocalWakeLock =
          mLocalPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
    }
    if (mLocalWakeLock != null) {
      mLocalWakeLock.acquire();
    }
  }

  private void setScreenOn() {
    isCasedByWakeLock = false;
    if (mLocalWakeLock != null) {
      mLocalWakeLock.setReferenceCounted(false);
      mLocalWakeLock.release();
      mLocalWakeLock = null;
    }
  }

  @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  class HeadsetReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      switch (action) {
        //插入和拔出耳机会触发此广播
        case Intent.ACTION_HEADSET_PLUG:
          int state = intent.getIntExtra("state", 0);
          if (state == 1) {
            isWiredHeadsetOn = true;
          } else if (state == 0) {
            isWiredHeadsetOn = false;
          }
          break;
        case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
          BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
          if (BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(
              BluetoothProfile.HEADSET)) {
            isWiredHeadsetOn = false;
          } else if (BluetoothProfile.STATE_CONNECTED == adapter.getProfileConnectionState(
              BluetoothProfile.HEADSET)) {
            isWiredHeadsetOn = true;
          }
          break;
        default:
          break;
      }
    }
  }

  public boolean isCasedByWakeLock() {
    return isCasedByWakeLock;
  }
}
