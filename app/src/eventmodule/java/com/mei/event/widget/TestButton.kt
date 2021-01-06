package com.mei.event.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.TextView

/**
 * @date 2021/1/5
 * @author mxb
 * @desc 自定义一个button
 * @desired
 */
class TestButton : TextView {

    private var mScaledTouchSlop: Int = 0
    private var mLastX = 0
    private var mLastY = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    companion object {
        private const val TAG = "TestButton"
    }

    private fun initView(context: Context) {
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        Log.i(TAG, "initView: mScaledTouchSlop=$mScaledTouchSlop")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        var x = event.rawX.toInt()
        var y = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var deltaX = x - mLastX
                var deltaY = y - mLastY
                Log.i(TAG, "onTouchEvent: deltaX=$deltaX;deltaY=$deltaY")
                var translationX = translationX + deltaX
                var translationY = translationY + deltaY
                setTranslationX(translationX)
                setTranslationY(translationY)
            }

            else -> {

            }
        }
        mLastX = x
        mLastY = y
        return true
    }

}