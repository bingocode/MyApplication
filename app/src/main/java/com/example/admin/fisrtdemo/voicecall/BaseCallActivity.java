package com.example.admin.fisrtdemo.voicecall;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 创建时间: 2018/08/10 09:33 <br>
 * 作者: zengbin <br>
 * 描述:呼叫界面的View , Activity要换成ChatUIBaseActivity
 */
public abstract class BaseCallActivity<V, T extends BaseCallPresenter<V>> extends Activity implements ICall.ICallView {
  protected T mPresenter;

  @SuppressWarnings("unchecked")
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = createPresenter();
    mPresenter.attachView((V) this);
  }

  @Override protected void onDestroy() {
    mPresenter.detachView();
    super.onDestroy();
  }

  protected abstract T createPresenter();
}
