package com.example.admin.fisrtdemo.jnitest;

/**
 * 创建时间: 2019/05/27 15:18 <br>
 * 作者: zengbin <br>
 * 描述:
 */
public class NDKTools {
  static {
    System.loadLibrary("native-lib");
  }
  public static native String getStringFromNDK();
}
