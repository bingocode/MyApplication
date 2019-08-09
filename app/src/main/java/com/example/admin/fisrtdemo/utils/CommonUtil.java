package com.example.admin.fisrtdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * 创建时间: 2018/08/27 16:08 <br>
 * 作者: zengbin <br>
 * 描述:工具类
 */
public class CommonUtil {
  private static final String TAG = "CommonUtil";
  public static boolean isNotifyEnable(Context context) {
    NotificationManagerCompat manager = NotificationManagerCompat.from(context.getApplicationContext());
    Log.i(TAG, "isNotify = " + manager.areNotificationsEnabled());
    return manager.areNotificationsEnabled();
  }

  public static void goSystemSetting(Context context, String settingAction) {
    Intent intent = new Intent();
    intent.setAction(settingAction);
    intent.setData(Uri.parse("package:" + context.getPackageName()));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
