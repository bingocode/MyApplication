package com.example.admin.fisrtdemo.voicecall;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.event.NetWorkEvent;
import com.example.admin.fisrtdemo.event.PermissionEvent;
import com.example.admin.fisrtdemo.event.ShowLargeEvent;
import com.example.admin.fisrtdemo.utils.CommonUtil;
import com.example.admin.fisrtdemo.utils.FloatWindowManager;
import com.whu.zengbin.mutiview.util.LogUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VoiceCallActivity extends BaseCallActivity implements TimeCountHelper.ITimeUpdate, TelPhoneReceiver.PhoneStateCallBack {
  private static final String TAG = "VoiceCallActivity";
  private static final int REQUEST_CODE = 10001;
  public static final String CALL_TYPE_CODE = "voice_call_type";
  public static final String CALL_VIEW_STATE_CODE = "voice_call_state";
  private boolean isCheckingPermission = false;

  /**
   * 掌链通话界面
   */
  public static final int ZHANGLIANE_CALL = 1;

  /**
   * 贝壳通话界面
   */
  public static final int BEIKE_CALL = 2;

  /**
   * Link通话界面
   */
  public static final int LINK_CALL = 3;


  @Override public void updatetime(String timeStr) {
    mTime.setText(timeStr);
    mtimeHandler.postDelayed(mTimerRunnable, 1000);
    FloatWindowManager.setTimetv(timeStr);
  }

  @Override public void onIdle() {
    Toast.makeText(this, "空闲中：CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();

  }

  @Override public void onRing() {
    Toast.makeText(this, "响铃中：CALL_STATE_RINGING", Toast.LENGTH_SHORT).show();
  }

  @Override public void onOffhook() {
    Toast.makeText(this, "通话中：CALL_STATE_OFFHOOK", Toast.LENGTH_SHORT).show();
  }

  @Retention(RetentionPolicy.SOURCE) @IntDef({
      ZHANGLIANE_CALL, BEIKE_CALL, LINK_CALL
  }) public @interface SHOW_CALL {
  }

  /**
   * 等待呼叫界面状态
   */
  public static final int WAITING_CALL_VIEW_STATE = 1;
  /**
   * 正在通话界面
   */
  public static final int TALKING_CALL_VIEW_STATE = 2;

  /**
   * 通话结束界面
   */
  public static final int FINISHED_CALL_VIEW_STATE = 3;

  @Retention(RetentionPolicy.SOURCE) @IntDef({
      WAITING_CALL_VIEW_STATE, TALKING_CALL_VIEW_STATE, FINISHED_CALL_VIEW_STATE
  }) public @interface CALL_VIEW_STATE {
  }

  private int mCallViewState = WAITING_CALL_VIEW_STATE;

  private LinearLayout mSmallest;
  private LinearLayout mHouseCard;
  private LinearLayout mHouseReport;
  private ImageView mHeadImg;
  private TextView mName;
  private TextView mHint;
  private TextView mSuggest;
  private TextView mTimetext;
  private TextView mTime;

  /**
   * 底部按钮
   */

  private LinearLayout mCenterIcon;
  private ImageView mCenterIconImg;
  private TextView mCenterIconTv;
  
  private LinearLayout mLeftIcon;
  private ImageView mLeftIconImg;
  private TextView mLeftIconTv;

  private LinearLayout mRightIcon;
  private ImageView mRightIconImg;
  private TextView mRightIconTv;

  //判断是否是挂断关闭通话
  private boolean isExit = false;
  int mCallType;

  private String mPhNumber = "10086";

  private Vibrator mVibrator;
  private long[] patter = {1000, 1000, 1000, 1000};
  private MediaPlayer mMediaPlayer;

  private Handler mtimeHandler = new Handler();
  private TimeCountHelper mTimerRunnable;
  private CallAudioController mCallAudioController;
  private boolean isCasedByWakeLock = false;

  private IntentFilter intentFilter;
  private NetworkChangeReceiver networkChangeReceiver;
  private TelPhoneReceiver mTelListener;
  private HomeListener homeListener;



  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.chatui_activity_voice_call);
    Intent intent = getIntent();
    mCallType = intent.getIntExtra(CALL_TYPE_CODE,ZHANGLIANE_CALL);
    EventBus.getDefault().register(this);

    mTimerRunnable = TimeCountHelper.getInstance();
    mTimerRunnable.setTimeUpdate(this);
    initView();
    mCallAudioController = new CallAudioController(this);

    intentFilter = new IntentFilter();
    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
    intentFilter.addAction("android.net.wifi.STATE_CHANGE");
    networkChangeReceiver = new NetworkChangeReceiver();
    registerReceiver(networkChangeReceiver, intentFilter);
    mTelListener = new TelPhoneReceiver(this,this);
    IntentFilter filter=new IntentFilter();
    filter.addAction("android.intent.action.PHONE_STATE");
    filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
    registerReceiver(mTelListener, filter);

    setCallViewState(WAITING_CALL_VIEW_STATE);
    if (CommonUtil.isNotifyEnable(this)) {
      Toast.makeText(this, "打开通话界面", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, "无通知栏权限", Toast.LENGTH_SHORT).show();
      final AlertDialog dialog = new AlertDialog.Builder(this)
          .setTitle("权限申请")
          .setMessage("去授权通知栏权限")
          .setPositiveButton(getString(R.string.chatui_group_detail_ok_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              CommonUtil.goSystemSetting(VoiceCallActivity.this,Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            }
          })
          .create();
      dialog.show();
    }
  }

  @Override protected void onStart() {
    super.onStart();
    if (isCasedByWakeLock) {
      Log.i(TAG, "onStart1");
      return;
    }
    LogUtil.i(TAG, "Onstart" + "  and Floatishow "+FloatWindowManager.isShown );
    FloatWindowManager.hideFloatWindow();
  }

  @Override protected void onResume() {
    super.onResume();
    if (isCasedByWakeLock) {
      Log.i(TAG, "onResume1");
      isCasedByWakeLock = false;
      return;
    }
    LogUtil.i(TAG, "onResume");
    mCallAudioController.register();
    homeListener = new HomeListener(this);
    homeListener.setOnHomePressedListener(new HomeListener.OnHomePressedListener() {
      @Override
      public void onHomePressed() {
        //处理按了home后的事
        Log.e("tag==","Home键");
        Toast.makeText(VoiceCallActivity.this, "Home键", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onHomeLongPressed() {
        //处理按了任务键后的事
        Log.e("tag==","任务切换键");
        Toast.makeText(VoiceCallActivity.this, "任务切换键", Toast.LENGTH_SHORT).show();
      }
    });
    homeListener.startWatch();//注册广播
    super.onResume();
  }

  @Override protected void onPause() {
    if(mCallAudioController.isCasedByWakeLock()) {
      isCasedByWakeLock = true;
      LogUtil.i(TAG, "onPause1");
      super.onPause();
      return;
    }
    LogUtil.i(TAG, "onPause");
    mCallAudioController.unRegister();
    super.onPause();
  }

  @Override protected void onStop() {
    homeListener.stopWatch();//注销广播
    if (isCasedByWakeLock) {
      LogUtil.i(TAG, "onStop1");
      super.onStop();
      return;
    }
    LogUtil.i(TAG, "onStop");
    if(!isExit) {
      FloatWindowManager.showFloatWindow(VoiceCallActivity.this, R.layout.chatui_item_tel_small,mCallViewState);
    }
    super.onStop();
  }

  @Override protected void onDestroy() {
    LogUtil.i(TAG, "onDestroy");
    mtimeHandler.removeCallbacksAndMessages(null);
    mCallAudioController.destroy();
    unregisterReceiver(networkChangeReceiver);
    mTelListener.destory();
    unregisterReceiver(mTelListener);
    mTelListener = null;
    EventBus.getDefault().unregister(this);
    super.onDestroy();

  }

  @Override public void onBackPressed() {

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      if (Build.VERSION.SDK_INT >= 23) {
        if (Settings.canDrawOverlays(this)) {
          Log.i(TAG, "onActivityResult granted");
          isCheckingPermission = false;
          mPresenter.smallestCall();
        }
      }
    }

  }

  @Override protected BaseCallPresenter createPresenter() {
    return new VoiceCallPresenter();
  }

  private void initView() {
    mSmallest = (LinearLayout) findViewById(R.id.ll_smallest);
    mHouseCard = (LinearLayout) findViewById(R.id.chat_item_house_card);
    mHouseReport = (LinearLayout) findViewById(R.id.chat_item_house_report);
    mHeadImg = (ImageView) findViewById(R.id.iv_head);
    mName = (TextView) findViewById(R.id.tv_name);
    mHint = (TextView) findViewById(R.id.tv_hint);
    mSuggest = (TextView) findViewById(R.id.tv_suggest);
    mTimetext = (TextView) findViewById(R.id.tv_time_text);
    mTime = (TextView) findViewById(R.id.tv_time);

    mCenterIcon = (LinearLayout) findViewById(R.id.ll_center_icon);
    mCenterIconImg = (ImageView) findViewById(R.id.iv_center_icon);
    mCenterIconTv = (TextView) findViewById(R.id.tv_center_icon);

    mLeftIcon = (LinearLayout) findViewById(R.id.ll_left_icon);
    mLeftIconImg = (ImageView) findViewById(R.id.iv_left_icon);
    mLeftIconTv = (TextView) findViewById(R.id.tv_left_icon);

    mRightIcon = (LinearLayout) findViewById(R.id.ll_right_icon);
    mRightIconImg = (ImageView) findViewById(R.id.iv_right_icon);
    mRightIconTv = (TextView) findViewById(R.id.tv_right_icon);

    ImageView mHouseImg = ViewHelper.findView(mHouseCard,R.id.iv_second_hand_house_image);
    TextView mHouseTitle = ViewHelper.findView(mHouseCard,R.id.tv_house_source_title);
    TextView mHouseDesc = ViewHelper.findView(mHouseCard,R.id.tv_house_source_description);
    TextView mHouseType = ViewHelper.findView(mHouseCard,R.id.tv_house_type);
    TextView mBrandType = ViewHelper.findView(mHouseCard,R.id.tv_brand_type);
    TextView mSaleType = ViewHelper.findView(mHouseCard,R.id.tv_sale_type);
    TextView mPirce = ViewHelper.findView(mHouseCard,R.id.tv_house_source_price_tag);

    TextView mPriceBias = ViewHelper.findView(mHouseReport,R.id.tv_price_bias);
    TextView mHomeBias = ViewHelper.findView(mHouseReport,R.id.tv_home_bias);
    TextView mBussinessBias = ViewHelper.findView(mHouseReport,R.id.tv_business_bias);

    mHouseImg.setImageResource(R.drawable.hource_handle);
    mHouseTitle.setText("上地东里小区");
    mHouseDesc.setText("4-2-2-2 · 145平 · 06/25 · 南北");
    mHouseType.setText("学区房");
    mBrandType.setText("掌上链家");
    mSaleType.setText("租售");
    mPirce.setText("3452万");

    mPriceBias.setText("价格偏好：300-400万");
    mHomeBias.setText("居室偏好：3居室");
    mBussinessBias.setText("商圈偏好：中关村");

    mSmallest.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        checkPermissionAndSmall();
      }
    });
  }

  private void configView() {
    mCallAudioController.setAudioPlaying(true);
    playRing(this);
    //测试代码c端用户拨打电话后被接通
    mHeadImg.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mPresenter.answerCall();
      }
    });

    switch (mCallType) {
      case ZHANGLIANE_CALL:
        mHeadImg.setVisibility(View.VISIBLE);
        mHeadImg.setImageResource(R.mipmap.chatui_img_default_avatar);
        mName.setVisibility(View.VISIBLE);
        mHint.setVisibility(View.VISIBLE);
        mHint.setText(R.string.chatui_voice_call_waiting_hint);

        mLeftIconImg.setImageResource(R.mipmap.chatui_ic_switch_phone);
        mLeftIconTv.setText(R.string.chatui_voice_call_switch_phone);

        mLeftIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            switchToPhoneCall(mPhNumber);
          }
        });

        mRightIconImg.setImageResource(R.mipmap.chatui_ic_hang_up);
        mRightIconTv.setText(R.string.chatui_voice_call_hang_up);
        mRightIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mPresenter.exitCall();
          }
        });
        break;

      case BEIKE_CALL:
        mHeadImg.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mHint.setVisibility(View.VISIBLE);
        mHint.setText(R.string.chatui_voice_call_waiting_hint);

        mLeftIconImg.setImageResource(R.mipmap.chatui_ic_switch_phone_1);
        mLeftIconTv.setText(R.string.chatui_voice_call_switch_phone);
        mLeftIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            switchToPhoneCall(mPhNumber);
          }
        });

        mRightIconImg.setImageResource(R.mipmap.chatui_ic_hang_up);
        mRightIconTv.setText(R.string.chatui_voice_call_hang_up);
        mRightIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mPresenter.exitCall();
          }
        });
        break;

      case LINK_CALL:
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(patter,0);
        mHouseCard.setVisibility(View.VISIBLE);
        mHouseReport.setVisibility(View.VISIBLE);
        mTimetext.setVisibility(View.VISIBLE);
        mTimetext.setText("连接中");

        mLeftIconImg.setImageResource(R.mipmap.chatui_ic_hang_up);
        mLeftIconTv.setText(R.string.chatui_voice_call_hang_up);
        mLeftIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mPresenter.exitCall();
          }
        });

        mRightIconImg.setImageResource(R.mipmap.chatui_ic_answer);
        mRightIconTv.setText(R.string.chatui_voice_call_answer);
        mRightIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mPresenter.answerCall();
          }
        });
        break;
    }
  }

  /**
   * 切换到正在通话界面
   */
  private void switchToConnectedView() {
    //测试代码（ 取消监听）
    mHeadImg.setClickable(false);

    stopRing();
    switch (mCallType) {
      case ZHANGLIANE_CALL:
        mTimetext.setVisibility(View.VISIBLE);
        mTime.setVisibility(View.VISIBLE);

        mCenterIcon.setVisibility(View.VISIBLE);
        mCenterIconImg.setImageResource(R.mipmap.chatui_ic_hang_up);
        mCenterIconTv.setText(R.string.chatui_voice_call_hang_up);
        mCenterIcon.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          setCallViewState(FINISHED_CALL_VIEW_STATE);
        }
      });

        mRightIconImg.setImageResource(R.mipmap.chatui_ic_handsfree);
        mRightIconTv.setText(R.string.chatui_voice_call_hands_free);
        mRightIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            openHandsFree();
          }
        });

        break;

      case BEIKE_CALL:
        mTimetext.setVisibility(View.VISIBLE);
        mTime.setVisibility(View.VISIBLE);

        mCenterIcon.setVisibility(View.VISIBLE);
        mCenterIconImg.setImageResource(R.mipmap.chatui_ic_hang_up);
        mCenterIconTv.setText(R.string.chatui_voice_call_hang_up);
        mCenterIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            setCallViewState(FINISHED_CALL_VIEW_STATE);
          }
        });

        mRightIconImg.setImageResource(R.mipmap.chatui_ic_handsfree);
        mRightIconTv.setText(R.string.chatui_voice_call_hands_free);
        mRightIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            openHandsFree();
          }
        });
        break;

      case LINK_CALL:
        mVibrator.cancel();
        mTimetext.setVisibility(View.VISIBLE);
        mTimetext.setText("正在通话");
        mTime.setVisibility(View.VISIBLE);

        mLeftIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            setCallViewState(FINISHED_CALL_VIEW_STATE);
          }
        });

        mRightIconImg.setImageResource(R.mipmap.chatui_ic_handsfree);
        mRightIconTv.setText(R.string.chatui_voice_call_hands_free);
        mRightIcon.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            openHandsFree();
          }
        });
        break;

    }
    mtimeHandler.postDelayed(mTimerRunnable, 1000);
  }

  /**
   * 切换到通话结束界面
   */
  private void switchToFinishedView() {
    mCallAudioController.setAudioPlaying(false);
    mTimerRunnable.exitTime();
    //取消监听
    mCenterIcon.setClickable(false);

    switch (mCallType) {
      case ZHANGLIANE_CALL:
        mTimetext.setVisibility(View.INVISIBLE);
        mSuggest.setVisibility(View.VISIBLE);
        mSuggest.setText(R.string.chatui_voice_call_over);

        mLeftIcon.setVisibility(View.INVISIBLE);
        mRightIcon.setVisibility(View.INVISIBLE);
        mCenterIconImg.setImageResource(R.mipmap.chatui_ic_hang_up_1);
        break;

      case BEIKE_CALL:
        mTimetext.setVisibility(View.INVISIBLE);
        mSuggest.setVisibility(View.VISIBLE);
        mSuggest.setText(R.string.chatui_voice_call_over);

        mLeftIcon.setVisibility(View.INVISIBLE);
        mRightIcon.setVisibility(View.INVISIBLE);
        mCenterIconImg.setImageResource(R.mipmap.chatui_ic_hang_up_1);
        break;

      case LINK_CALL:
        mTimetext.setVisibility(View.INVISIBLE);
        mSuggest.setVisibility(View.VISIBLE);
        mSuggest.setText(R.string.chatui_voice_call_over);

        mLeftIcon.setVisibility(View.INVISIBLE);
        mRightIcon.setVisibility(View.INVISIBLE);
        break;
    }
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        mPresenter.exitCall();
      }
    },800);
  }

  @Override public void answerCallView() {
    setCallViewState(TALKING_CALL_VIEW_STATE);
  }

  @Override public void exitCallView() {
      stopRing();
      if (mCallType == LINK_CALL) {
        mVibrator.cancel();
      }
    if(mTimerRunnable.isCounting()) {
      mTimerRunnable.exitTime();
    }
    isExit = true;
    FloatWindowManager.hideFloatWindow();
    finish();
  }

  @Override public void smallestCallView() {
    moveTaskToBack(true);
  }

  @Override public void setCallViewState(int state) {
    mCallViewState = state;
    switch (state) {
      case WAITING_CALL_VIEW_STATE:
        configView();
        break;
      case TALKING_CALL_VIEW_STATE:
        switchToConnectedView();
        break;
      case FINISHED_CALL_VIEW_STATE:
        switchToFinishedView();
        break;
    }
  }


  private void checkPermissionAndSmall() {
    if (Build.VERSION.SDK_INT >= 23) {
      if (!Settings.canDrawOverlays(this)) {
        //若没有权限，提示获取.
        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        final AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(getString(R.string.chatui_voice_call_apply_permission))
            .setMessage(getString(R.string.chatui_voice_call_need_float_permission))
            .setPositiveButton(getString(R.string.chatui_group_detail_ok_btn), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE);
              }
            })
            .create();
        dialog.show();
        EventBus.getDefault().post(new PermissionEvent("need floatWindow Permission"));
      } else {
        mPresenter.smallestCall();
      }
    } else {
      mPresenter.smallestCall();
    }
  }

  private void playRing(final Activity activity){
    try {
      mMediaPlayer = new MediaPlayer();
      AssetFileDescriptor fd = getAssets().openFd("call_music.mp3");
      mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),fd.getLength());
      mMediaPlayer.setLooping(true);
      mMediaPlayer.prepare() ;
      mMediaPlayer.start();
      fd.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void stopRing(){
    if (mMediaPlayer!=null){
      try {
        if (mMediaPlayer.isPlaying()){
          mMediaPlayer.stop();
          mMediaPlayer.release();
        }
      } catch (IllegalStateException e) {
        Log.e(TAG,"stop Ring:" + e);
        mMediaPlayer = null;
      }
    }
  }

  private void switchToPhoneCall(String phoneNum) {
    Intent intent = new Intent(Intent.ACTION_DIAL);
    Uri data = Uri.parse("tel:" + phoneNum);
    intent.setData(data);
    mPresenter.exitCall();
    startActivity(intent);
  }

  private void showSwitchPhoneSuggest(String suggestText) {
    if(!TextUtils.isEmpty(suggestText)) {
      mSuggest.setVisibility(View.VISIBLE);
      mSuggest.setText(suggestText);
    } else {
      mSuggest.setVisibility(View.INVISIBLE);
    }
  }

  private void openHandsFree() {
    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.setMode(AudioManager.MODE_IN_CALL);
    audioManager.setSpeakerphoneOn(!audioManager.isSpeakerphoneOn());
    if(audioManager.isSpeakerphoneOn()) {
      mRightIconImg.setImageResource(R.mipmap.chatui_ic_handsfree_opening);
    } else {
      mRightIconImg.setImageResource(R.mipmap.chatui_ic_handsfree);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onNetWorkConditionEvent(NetWorkEvent event) {
     showSwitchPhoneSuggest(event.networksuggest);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onShowLargeEvent(ShowLargeEvent event) {
    moveTaskToBack(false);
  }

}
