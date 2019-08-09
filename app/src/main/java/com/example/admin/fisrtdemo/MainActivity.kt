package com.example.admin.fisrtdemo

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.example.admin.fisrtdemo.jnitest.JniTestActivity
import com.example.admin.fisrtdemo.voicecall.MyService
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.BEIKE_CALL
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.CALL_TYPE_CODE
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.LINK_CALL
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.ZHANGLIANE_CALL
import com.example.admin.fisrtdemo.voicecall.VoipActivity
import kotlinx.android.synthetic.main.activity_main.button1
import kotlinx.android.synthetic.main.activity_main.button2
import kotlinx.android.synthetic.main.activity_main.button3
import kotlinx.android.synthetic.main.activity_main.button4
import kotlinx.android.synthetic.main.activity_main.button5
import kotlinx.android.synthetic.main.activity_main.button6

class MainActivity : BaseActivity(), View.OnClickListener {

  private var mExitTime: Long = 0
  // private var mIntent: Intent =

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initView()
  }

  private fun initView() {
    button1.setOnClickListener(this)
    button2.setOnClickListener(this)
    button3.setOnClickListener(this)
    button4.setOnClickListener(this)
    button5.setOnClickListener(this)
    button6.setOnClickListener(this)
  }

  override fun onClick(v: View) {
    when (v) {
      button1 -> FirstActivity.actionStart(this@MainActivity)
      button2 -> SecondActivity.actionStart(this@MainActivity)
      button3 ->
        MyListviewActivity.start(this@MainActivity)
      button3 ->
        startService(Intent(this@MainActivity, MyService::class.java)) //启动后台服务
      button4 ->
      {
        JniTestActivity.start(this@MainActivity)
        // startActivity(Intent(this@MainActivity, ThirdActivity::class.java))
//        val mIntent: Intent = Intent(this@MainActivity, VoiceCallActivity::class.java)
//        mIntent.putExtra(CALL_TYPE_CODE, ZHANGLIANE_CALL)
//        startActivity(mIntent)
      }
      button5 ->
      {
        startActivity(Intent(this@MainActivity,CustomViewActivity::class.java))
      }
      button6 -> {
        var a :Intent = Intent(this@MainActivity, VoipActivity::class.java)
        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)
      }
      else -> {
      }
    }

  }

  override fun onKeyDown(
    keyCode: Int,
    event: KeyEvent
  ): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {

      exit()
      return true
    }
    return super.onKeyDown(keyCode, event)
  }

  fun exit() {
    if (System.currentTimeMillis() - mExitTime > 2000) {
      Toast.makeText(this@MainActivity, "再按一次退出", Toast.LENGTH_SHORT).show()
      mExitTime = System.currentTimeMillis()
    } else {
      finish()
      //System.exit(0)
    }
  }
}
