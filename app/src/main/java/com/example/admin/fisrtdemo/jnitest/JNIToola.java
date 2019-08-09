package com.example.admin.fisrtdemo.jnitest;

/**
 * 创建时间: 2019/05/30 15:51 <br>
 * 作者: zengbin <br>
 * 描述:
 */
public class JNIToola {
  static {
    System.loadLibrary("caculate_jni");
  }

  //加法
  public static native int  add(int a,int b);

  //减法
  public static native int sub(int a,int b);

  //乘法
  public static native int mul(int a,int b);

  //除法
  public static native int div(int a,int b);
}
