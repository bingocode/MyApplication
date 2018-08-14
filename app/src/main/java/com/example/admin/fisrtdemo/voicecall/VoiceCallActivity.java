package com.example.admin.fisrtdemo.voicecall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.service.FloatWindowService;
import com.example.admin.fisrtdemo.utils.FloatWindowManager;
import com.whu.zengbin.mutiview.LogUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VoiceCallActivity extends BaseCallActivity implements TimeCountHelper.ITimeUpdate {
  private static final String TAG = "VoiceCallActivity";
  private static final int REQUEST_CODE = 10001;
  public static final String CALL_TYPE_CODE = "voice_call_type";
  public static final String CALL_VIEW_STATE_CODE = "voice_call_state";

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
  private Intent mServiceIntent;
  int mCallType;

  private String mPhNumber = "10086";

  private Vibrator mVibrator;
  private long[] patter = {1000, 1000, 1000, 1000};
  private MediaPlayer mMediaPlayer;

  private Handler mtimeHandler = new Handler();
  private TimeCountHelper mTimerRunnable;
  private CallAudioController mCallAudioController;
  private boolean isCasedByWakeLock = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chatui_activity_voice_call);
    Intent intent = getIntent();
    mCallType = intent.getIntExtra(CALL_TYPE_CODE,ZHANGLIANE_CALL);
    mServiceIntent = new Intent(this, FloatWindowService.class);
    mTimerRunnable = TimeCountHelper.getInstance();
    mTimerRunnable.setTimeUpdate(this);
    initView();
    mCallAudioController = new CallAudioController(this);
    setCallViewState(WAITING_CALL_VIEW_STATE);
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
    if (isCasedByWakeLock) {
      LogUtil.i(TAG, "onStop1");
      super.onStop();
      return;
    }
    LogUtil.i(TAG, "onStop");
    if(!isExit) {
      mServiceIntent.putExtra(CALL_VIEW_STATE_CODE,mCallViewState);
      startService(mServiceIntent);
    }
    super.onStop();
  }

  @Override protected void onDestroy() {
    LogUtil.i(TAG, "onDestroy");
    mtimeHandler.removeCallbacks(mTimerRunnable);
    mCallAudioController.destroy();
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
        showSwitchPhoneSuggest(true);
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
    if (mCallViewState == WAITING_CALL_VIEW_STATE) {
      stopRing();
      if (mCallType == LINK_CALL) {
        mVibrator.cancel();
      }
    }
    if(mTimerRunnable.isCounting()) {
      mTimerRunnable.exitTime();
    }
    isExit = true;
    stopService(mServiceIntent);
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
                VoiceCallActivity.this.startActivity(intent);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE);
              }
            })
            .create();
        dialog.show();
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void stopRing(){
    if (mMediaPlayer!=null){
      if (mMediaPlayer.isPlaying()){
        mMediaPlayer.stop();
        mMediaPlayer.release();
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

  private void showSwitchPhoneSuggest(boolean isShow) {
    if(isShow) {
      mSuggest.setVisibility(View.VISIBLE);
      mSuggest.setText(R.string.chatui_voice_call_net_problem_suggest);
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

  }
