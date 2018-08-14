package com.example.admin.fisrtdemo.voicecall;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 创建时间: 2018/08/10 09:35 <br>
 * 作者: zengbin <br>
 * 描述:通话抽象类
 */
public abstract class BaseCallPresenter<T> implements ICall.ICallPresenter {
  protected Reference<T> mView;

  public void attachView(T view) {
    mView = new WeakReference<T>(view);
  }

  protected T getView() {
    return mView.get();
  }

  public boolean isViewAttached() {
    return mView != null && mView.get() != null;
  }

  public void detachView() {
    if(mView != null) {
      mView.clear();
      mView = null;
    }
  }

}
