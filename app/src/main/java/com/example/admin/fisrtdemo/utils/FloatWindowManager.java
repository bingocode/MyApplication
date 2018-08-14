package com.example.admin.fisrtdemo.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import android.widget.ImageView;
import android.widget.TextView;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.voicecall.TimeCountHelper;
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity;

/**
 * 创建时间: 2018/08/08 10:11 <br>
 * 作者: zengbin <br>
 * 描述:悬浮窗管理类
 */
public class FloatWindowManager {
  private static final String TAG = "FloatWindowManager";
  private static View mView = null;
  private static TextView mTimetv;
  private static WindowManager mWindowManager = null;
  private static Context mContext = null;
  public static Boolean isShown = false;
  private static WindowManager.LayoutParams params;
  
  static int sPhoneWidth;
  static int sPhoneHeight;
  
  /**
   * 移动的位置
   */
  private static int startX;
  private static int startY;
  private static int lastX;
  private static int lastY;
  private static int finalX = 0;
  private static int finalY = 0;

  /**
   * 显示弹出框
   */
  public static void showFloatWindow(final Context context, int resId,@VoiceCallActivity.CALL_VIEW_STATE int state) {
    if (isShown) {
      Log.i(TAG, "return cause already shown");
      return;
    }
    isShown = true;
    Log.i(TAG, "showFloatWindow");
    // 获取应用的Context
    mContext = context.getApplicationContext();
    // 获取WindowManager
    mWindowManager = (WindowManager) mContext
        .getSystemService(Context.WINDOW_SERVICE);

    sPhoneWidth = mWindowManager.getDefaultDisplay().getWidth();
    sPhoneHeight = mWindowManager.getDefaultDisplay().getHeight();

    mView = setUpView(context,resId);
    mTimetv = (TextView) mView.findViewById(R.id.tv_small_text);
    if (state == VoiceCallActivity.WAITING_CALL_VIEW_STATE) {
      ((ImageView) mView.findViewById(R.id.iv_small_window)).setImageResource(R.mipmap.chatui_ic_small_window_waiting);
      mTimetv.setText(R.string.chatui_voice_call_waiting_answer);
    } else {
      ((ImageView)mView.findViewById(R.id.iv_small_window)).setImageResource(R.mipmap.chatui_ic_small_window_calling);
      mTimetv.setText(TimeCountHelper.getFormatHMS(TimeCountHelper.getInstance().time));
    }
    params = new WindowManager.LayoutParams();
    // 类型
    params.type = LayoutParams.TYPE_SYSTEM_ALERT;
    // 设置flag
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
    // FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
    // FLAG_NOT_TOUCH_MODAL 不阻塞事件传递到后面的窗口

    // 不设置这个弹出框的透明遮罩显示为黑色
    params.format = PixelFormat.TRANSLUCENT;

    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.x = finalX;
    params.y = finalY;

    //以右上角为原点
    params.gravity = Gravity.TOP | Gravity.END;
    mWindowManager.addView(mView, params);


    mView.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:// 按下 事件
            startX = lastX = (int) event.getRawX();
            startY = lastY = (int) event.getRawY();
            break;
          case MotionEvent.ACTION_MOVE:// 移动 事件
            params.x = params.x - (int) (event.getRawX() - lastX);
            params.y = params.y + (int) (event.getRawY() - lastY);

            //边界设置(自动靠边)
            if(params.x <=0) {
              params.x =0;
            } else if (params.x > sPhoneWidth - mView.getWidth()) {
              params.x = sPhoneWidth -mView.getWidth();
            }
            if(params.y <=0) {
              params.y =0;
            } else if (params.y > sPhoneHeight - mView.getHeight()) {
              params.y = sPhoneHeight -mView.getHeight();
            }
            mWindowManager.updateViewLayout(v, params);
            lastX = (int) event.getRawX();
            lastY = (int) event.getRawY();
            break;
          case MotionEvent.ACTION_UP:
            //处理点击
            boolean isclick = Math.abs((int) event.getRawX() - startX) < 20 && Math.abs((int) event.getRawY() - startY) < 20;
            if (params.x > sPhoneWidth / 2- v.getWidth()/2) {
              //放手后移到左边
              moveAnim( params.x, sPhoneWidth - v.getWidth(), isclick);
            } else {
              //移到右边
              moveAnim( params.x, 0, isclick);
            }
            break;
        }
        return true;
      }

    });
  }

  public static void setTimetv(String timeStr) {
    if(isShown && mView != null && mTimetv !=null){
      mTimetv.setText(timeStr);
    }
  }

  private static void moveAnim( int oldX, int newX, final boolean isclick) {
    ValueAnimator anim = ValueAnimator.ofInt(oldX,  newX);
    anim.setDuration(200);
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        //设置坐标
          params.x = (int) animation.getAnimatedValue();
        mWindowManager.updateViewLayout(mView, params);
      }
    });
    anim.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animator) {}

      @Override
      public void onAnimationEnd(Animator animator) {
        finalX = params.x;
        finalY = params.y;
        if(isclick) {
          Log.i(TAG, "add view");
          FloatWindowManager.hideFloatWindow();
          mContext.startActivity(new Intent(mContext, VoiceCallActivity.class));
        }
      }

      @Override
      public void onAnimationCancel(Animator animator) {}

      @Override
      public void onAnimationRepeat(Animator animator) {}
    });
    anim.start();
  }

  /**
   * 隐藏弹出框
   */
  public static void hideFloatWindow() {
    Log.i(TAG, "hide " + isShown + ", " + mView);
    if (isShown &&  mView != null) {
      Log.i(TAG, "hideFloatWindow");
      mWindowManager.removeView(mView);
      mView = null;
      mTimetv = null;
      isShown = false;
    }
  }

  private static View setUpView(final Context context,int resId) {
    Log.i(TAG, "setUp view");
    View view = LayoutInflater.from(context).inflate(resId,
        null);
    return view;
  }


}

