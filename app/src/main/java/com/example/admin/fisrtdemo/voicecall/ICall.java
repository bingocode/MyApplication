package com.example.admin.fisrtdemo.voicecall;

/**
 * 创建时间: 2018/08/10 09:33 <br>
 * 作者: zengbin <br>
 * 描述:通话界面MVP接口
 */
public interface ICall {
  interface ICallView {

    /**
     * 接通通话界面
     */
    void answerCallView();

    /**
     * 退出通话界面
     */
    void exitCallView();

    /**
     * 最小化界面
     */
    void smallestCallView();

    /**
     *设置语音通话的界面状态（等待接通，通话中，结束状态）
     */
    void setCallViewState(@VoiceCallActivity.CALL_VIEW_STATE int state);

  }

  interface ICallPresenter {

    /**
     * 接通电话
     */
    void answerCall();

    /**
     * 退出通话界面
     */
    void exitCall();
    /**
     * 最小化界面
     */
    void smallestCall();
  }

}
