package com.example.admin.fisrtdemo.voicecall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.example.admin.fisrtdemo.event.PermissionEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建时间: 2018/08/10 10:21 <br>
 * 作者: zengbin <br>
 * 描述:通话逻辑Presenter
 */
public class VoiceCallPresenter extends BaseCallPresenter<VoiceCallActivity> {

  @Override public void exitCall() {
    //处理退出通话界面的逻辑，如发送请求等..
      getView().exitCallView();
  }

  @Override public void smallestCall() {
    //最小化界面的业务逻辑...
    getView().smallestCallView();
  }

  @Override public void answerCall() {
    //接通电话逻辑
    getView().answerCallView();
  }
}
