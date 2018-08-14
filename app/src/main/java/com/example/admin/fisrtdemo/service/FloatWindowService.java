package com.example.admin.fisrtdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.utils.FloatWindowManager;
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity;

import static android.app.PendingIntent.getActivity;
import static com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.CALL_VIEW_STATE_CODE;
import static com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.WAITING_CALL_VIEW_STATE;

/**
 * 创建时间: 2018/08/09 15:48 <br>
 * 作者: zengbin <br>
 * 描述:语音通话后台服务
 */
public class FloatWindowService extends Service{
  private static final String TAG = "FloatWindowService";
  private int mState = VoiceCallActivity.WAITING_CALL_VIEW_STATE;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG,"onCreate");
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Log.d(TAG,"onBind");
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG,"onStartCommand");
    mState = intent.getIntExtra(CALL_VIEW_STATE_CODE,WAITING_CALL_VIEW_STATE);
    showFlowWindow();
    //setFrontView();
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    Log.d(TAG,"onDestroy");

    FloatWindowManager.hideFloatWindow();
    super.onDestroy();
  }

  private void showFlowWindow() {
    FloatWindowManager.showFloatWindow(this, R.layout.chatui_item_tel_small,mState);
  }

  /**
   * 设置前台服务
   */
  //private void setFrontView() {
  //  Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
  //  Intent nfIntent = new Intent(this, VoiceCallActivity.class);
  //
  //  builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
  //      .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
  //  .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
  //  .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
  //  .setContentText("要显示的内容") // 设置上下文内容
  //  .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
  //
  //  Notification notification = builder.build(); // 获取构建好的Notification
  //  notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
  //  startForeground(110, notification);
  //
  //}

}
