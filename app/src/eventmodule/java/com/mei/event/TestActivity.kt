package com.mei.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mei.cuswidget.R

/**
 * @date 2021/1/5
 * @author mxb
 * @desc
 * @desired
 */
class TestActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private var mButton1: Button? = null
    private var mButton2: View? = null
    private var mCount = 0

    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    mCount++
                    if (mCount <= 30) {
                        Log.i("TestActivity", "handleMessage: 移动次数：$mCount")
                        var fraction: Float = mCount / 30f
                        var scrollX = (fraction * 100).toInt()
                        mButton1?.scrollTo(scrollX, 0)
                        sendEmptyMessageDelayed(1, 33)
                    }
                }
                else -> {

                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initView()
    }

    private fun initView() {
        mButton1 = findViewById(R.id.button1)
        mButton2 = findViewById(R.id.button2)
        mButton1?.setOnClickListener(this)
        mButton2?.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.button1) {
            mHandler.removeMessages(1)
            mHandler.sendEmptyMessageDelayed(1, 30)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        Toast.makeText(this, "long click", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}