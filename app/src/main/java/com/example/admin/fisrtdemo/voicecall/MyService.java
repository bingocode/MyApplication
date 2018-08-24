package com.example.admin.fisrtdemo.voicecall;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 模拟推送，在退出APP后的一段时间发送消息
 */
public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        sendMessage();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }


    /**
     * 模仿推送，发消息
     */
    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //管理锁屏的一个服务
                KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                String text = km.inKeyguardRestrictedInputMode() ? "锁屏了" : "屏幕亮着的";
                Log.i(TAG, "text: " + text);
                if (km.inKeyguardRestrictedInputMode()) {
                    Log.i(TAG, "onReceive:锁屏了 ");
                    //判断是否锁屏
                    Intent alarmIntent = new Intent(MyService.this, MessageActivity.class);
                    //在广播中启动Activity的context可能不是Activity对象，所以需要添加NEW_TASK的标志，否则启动时可能会报错。
                    alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(alarmIntent); //启动显示锁屏消息的activity
                }
            }
        }).start();
    }
}
