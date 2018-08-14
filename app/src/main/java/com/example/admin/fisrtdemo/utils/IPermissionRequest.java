package com.example.admin.fisrtdemo.utils;

/**
 * 创建时间: 2018/08/10 15:38 <br>
 * 作者: zengbin <br>
 * 描述:权限请求接口
 */
public interface IPermissionRequest {


  /**
   * 可设置请求权限请求码
   */
  int getPermissionsRequestCode();

  /**
   * 设置需要请求的权限
   */
  String[] getPermissions();

  /**
   * 请求权限成功回调
   */
  void requestPermissionsSuccess();

  /**
   * 请求权限失败回调
   */
  void requestPermissionsFail();


}
