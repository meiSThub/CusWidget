package com.mei.event.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ListView
import com.mei.event.utils.EventTypeUtils
import kotlin.math.abs

/**
 * @date 2021/1/6
 * @author mxb
 * @desc 自定义一个ListView，解决与自定义ViewPager的滑动冲突
 * @desired
 */
class MyListView(context: Context?, attrs: AttributeSet?) : ListView(context, attrs) {

    private var mLastX = 0
    private var mLastY = 0

    open var mViewPager: MyViewPager2? = null

    // 系统可以识别的最小的滑动距离
    private var mTouchSlop = 0;


    init {
        mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var x = ev.rawX.toInt()
        var y = ev.rawY.toInt()
        Log.i("InterceptTAG", "dispatchTouchEvent: ListView分发事件 ${EventTypeUtils.getEventType(ev)}")
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 因为默认情况下，父容器是允许拦截事件的，所以这里在ACTION_DOWN事件的时候，禁止父容器拦截事件
                // 所以在接下来的move事件，如果ListView可以消耗的话，因为禁止父容器拦截了，所以事件会传递到ListView
                // 从而ListView可以滑动
                mViewPager?.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                // 父容器不拦截
                var deltaX = x - mLastX
                var deltaY = y - mLastY
                Log.i("InterceptTAG", "dispatchTouchEvent: deltaX=$deltaX deltaY=$deltaY")
                if (abs(deltaX) > abs(deltaY) && abs(deltaX) > mTouchSlop) {
                    // 让父容器拦截事件
                    Log.i("InterceptTAG", "dispatchTouchEvent: 请求父容器拦截事件")
                    // 如果是横向滑动，则这个时候允许父容器拦截事件，则在一系列move事件的到来的时候，父容器可以
                    // 会调用onInterceptTouchEvent分发，进而会拦截move事件，所以父容器就可以横向滚动了
                    mViewPager?.requestDisallowInterceptTouchEvent(false)
                }
            }

            else -> {
                // //禁止父容器拦截事件
            }
        }
        mLastX = x
        mLastY = y

        return super.dispatchTouchEvent(ev)
    }
}