package com.example.admin.fisrtdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.admin.fisrtdemo.voicecall.MyService
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.BEIKE_CALL
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.CALL_TYPE_CODE
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.LINK_CALL
import com.example.admin.fisrtdemo.voicecall.VoiceCallActivity.ZHANGLIANE_CALL
import kotlinx.android.synthetic.main.activity_main.*

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
        var mIntent: Intent =  Intent(this@MainActivity, VoiceCallActivity::class.java)
        when (v) {
//            button1 -> FirstActivity.actionStart(this@MainActivity)
//            button2 -> SecondActivity.actionStart(this@MainActivity)
//            button3 ->
//                //                ActivityCollector.finishAll();
//                //                android.os.Process.killProcess(android.os.Process.myPid());
//                MyListviewActivity.start(this@MainActivity)
//            button3 ->
//                startService(Intent(this@MainActivity, MyService::class.java)) //启动后台服务
            button4 ->
               // startActivity(Intent(this@MainActivity, ThirdActivity::class.java))
                mIntent.putExtra(CALL_TYPE_CODE,ZHANGLIANE_CALL)
            button5 ->
                //startActivity(Intent(this@MainActivity,CustomViewActivity::class.java))
                mIntent.putExtra(CALL_TYPE_CODE,BEIKE_CALL)
            button6 ->
                {mIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                mIntent.putExtra(CALL_TYPE_CODE,LINK_CALL)}
            else -> {
            }
        }
        startActivity(mIntent)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
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
