package com.example.admin.fisrtdemo.jnitest;

/**
 * 创建时间: 2019/05/28 00:00 <br>
 * 作者: zengbin <br>
 * 描述:
 */
public class JniDemo1 {
  static {
    System.loadLibrary("native-lib2");
  }

  public native String sayHello(long n);

}

