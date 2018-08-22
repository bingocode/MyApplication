package com.example.admin.fisrtdemo.voicecall;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * 创建时间: 2018/08/14 20:13 <br>
 * 作者: zengbin <br>
 * 描述:
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


public class ViewHelper {
  public ViewHelper() {
  }

  public static <T extends View> T findView(@NonNull View root, @IdRes int viewId) {
    return root.findViewById(viewId);
  }

  public static <T extends View> T findView(@NonNull Activity root, @IdRes int viewId) {
    return root.findViewById(viewId);
  }

  public static int getWidth(View v) {
    int width = View.MeasureSpec.makeMeasureSpec(0, 0);
    int height = View.MeasureSpec.makeMeasureSpec(0, 0);
    v.measure(width, height);
    return v.getMeasuredWidth();
  }

  public static int getHeight(View v) {
    int width = View.MeasureSpec.makeMeasureSpec(0, 0);
    int height = View.MeasureSpec.makeMeasureSpec(0, 0);
    v.measure(width, height);
    return v.getMeasuredHeight();
  }

  public static void setTextViewLineExtra(final TextView textView, final float add, final float mult) {
    ViewTreeObserver vto = textView.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @SuppressLint({"NewApi"})
      public void onGlobalLayout() {
        int textLine = textView.getLineCount();
        if(textLine > 0) {
          if(Build.VERSION.SDK_INT >= 16) {
            textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          } else {
            textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          }

          if(textLine > 1) {
            textView.setLineSpacing(add, mult);
          } else {
            textView.setLineSpacing(0.0F, 1.0F);
          }
        }

      }
    });
  }
}
