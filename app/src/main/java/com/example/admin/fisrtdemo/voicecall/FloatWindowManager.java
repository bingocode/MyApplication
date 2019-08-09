package com.example.admin.fisrtdemo.voicecall;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 创建时间: 2019/08/05 19:47 <br>
 * 作者: zengbin <br>
 * 描述:
 */
public class FloatWindowManager {
  private static final String TAG = "FloatWindowManager";
  private static View mView = null;
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
  public static void showFloatWindow(final Context context, int resId) {
    if (isShown) {
      Log.i(TAG, "return cause already shown");
      return;
    }
    isShown = true;
    Log.i(TAG, "showFloatWindow1");
    // 获取应用的Context
    mContext = context.getApplicationContext();
    // 获取WindowManager
    mWindowManager = (WindowManager) mContext
        .getSystemService(Context.WINDOW_SERVICE);

    sPhoneWidth = mWindowManager.getDefaultDisplay().getWidth();
    sPhoneHeight = mWindowManager.getDefaultDisplay().getHeight();

    mView = setUpView(mContext, resId);
    params = new WindowManager.LayoutParams();
    // 类型
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    } else {
      params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    }
    // 设置flag
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

    /*
       params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
     */
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
        if (mView == null) {
          return true;
        }
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:// 按下 事件
            startX = lastX = (int) event.getRawX();
            startY = lastY = (int) event.getRawY();
            break;
          case MotionEvent.ACTION_MOVE:// 移动 事件
            params.x = params.x - (int) (event.getRawX() - lastX);
            params.y = params.y + (int) (event.getRawY() - lastY);

            //边界设置(自动靠边)
            if (params.x <= 0) {
              params.x = 0;
            } else if (params.x > sPhoneWidth - mView.getWidth()) {
              params.x = sPhoneWidth - mView.getWidth();
            }
            if (params.y <= 0) {
              params.y = 0;
            } else if (params.y > sPhoneHeight - mView.getHeight()) {
              params.y = sPhoneHeight - mView.getHeight();
            }
            mWindowManager.updateViewLayout(v, params);
            lastX = (int) event.getRawX();
            lastY = (int) event.getRawY();
            break;
          case MotionEvent.ACTION_UP:
            //处理点击
            boolean isclick = Math.abs((int) event.getRawX() - startX) < 20
                && Math.abs((int) event.getRawY() - startY) < 20;
            if (params.x > sPhoneWidth / 2 - v.getWidth() / 2) {
              //放手后移到左边
              moveAnim(params.x, sPhoneWidth - v.getWidth(), isclick);
            } else {
              //移到右边
              moveAnim(params.x, 0, isclick);
            }
            break;
        }
        return true;
      }
    });
  }

  private static void moveAnim(int oldX, int newX, final boolean isclick) {
    ValueAnimator anim = ValueAnimator.ofInt(oldX, newX);
    anim.setDuration(200);
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        //设置坐标
        params.x = (int) animation.getAnimatedValue();
        if (isShown && mView != null) {
          mWindowManager.updateViewLayout(mView, params);
        }
      }
    });
    anim.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animator) {
      }

      @Override
      public void onAnimationEnd(Animator animator) {
        finalX = params.x;
        finalY = params.y;
        if (isclick) {
          Log.i(TAG, "closeFloatWindow, show CallActivity");
          FloatWindowManager.hideFloatWindow();
          Intent intent = new Intent(mContext, VoipActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          mContext.startActivity(intent);
        }
      }

      @Override
      public void onAnimationCancel(Animator animator) {
      }

      @Override
      public void onAnimationRepeat(Animator animator) {
      }
    });
    anim.start();
  }

  /**
   * 隐藏弹出框
   */
  public static void hideFloatWindow() {
    if (isShown && mView != null) {
      Log.i(TAG, "hideFloatWindow");
      mWindowManager.removeView(mView);
      mView = null;
      isShown = false;
    }
  }

  private static View setUpView(final Context context, int resId) {
    View view = LayoutInflater.from(context).inflate(resId,
        null);
    return view;
  }
}

