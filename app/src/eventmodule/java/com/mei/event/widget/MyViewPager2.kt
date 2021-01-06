package com.mei.event.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewGroup
import android.widget.Scroller
import com.mei.event.utils.EventTypeUtils
import java.lang.reflect.Field
import kotlin.math.max
import kotlin.math.min

/**
 * @date 2021/1/6
 * @author mxb
 * @desc 自定义一个ViewPagers，并解决滑动冲突，内部部拦截法
 * @desired
 */
class MyViewPager2(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val TAG = "MyViewPager2"
    private var mLastX = 0
    private var mLastY = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mScroller: Scroller? = null
    private var mChildIndex = 0

    init {
        mScroller = Scroller(getContext())
        mVelocityTracker = VelocityTracker.obtain()
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 1. 测量子View的宽高
        var maxItemHeight = 0
        var totalItemWidth = 0
        for (i: Int in 0 until childCount) {
            var child = getChildAt(i)
            // 测量子View
            measureChildWithMargins(child, widthMeasureSpec, paddingLeft + paddingRight, heightMeasureSpec, paddingTop + paddingBottom)
            // 取最大的测量高度
            maxItemHeight = max(maxItemHeight, child.measuredHeight)
            var layoutParams = child.layoutParams as MarginLayoutParams
            totalItemWidth += child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
        }

        // 2. 计算自己的宽高
        var widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        var measureWidth: Int
        var measureHeight: Int
        if (childCount == 0) {
            measureWidth = widthSpecSize
            measureHeight = heightSpecSize
        } else {
            measureWidth = when (widthSpecMode) {
                MeasureSpec.AT_MOST -> totalItemWidth
                MeasureSpec.EXACTLY -> widthSpecSize
                else -> widthSpecSize
            }

            measureHeight = when (heightSpecMode) {
                MeasureSpec.AT_MOST -> maxItemHeight
                MeasureSpec.EXACTLY -> heightSpecSize
                else -> heightSpecSize
            }
        }

        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        for (i: Int in 0 until childCount) {
            var child = getChildAt(i)
            var layoutParams = child.layoutParams as MarginLayoutParams
            left += layoutParams.leftMargin
            var top = paddingTop + layoutParams.topMargin
            var right = left + child.measuredWidth
            var bottom = top + child.measuredHeight
            child.layout(left, top, right, bottom)

            left = right + layoutParams.rightMargin
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.rawX.toInt()
        val y = ev.rawY.toInt()

        if (ev.action == MotionEvent.ACTION_DOWN) {
            mLastX = x
            mLastY = y
            if (mScroller?.isFinished == false) {
                mScroller?.abortAnimation()
                return true
            }
            Log.i("InterceptTAG", "onInterceptTouchEvent: 父容器不拦截事件")
            return false
        }
        getView()
        Log.i("InterceptTAG", "onInterceptTouchEvent: 父容器拦截事件 ${EventTypeUtils.getEventType(ev)}")
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        mVelocityTracker?.addMovement(event)

        val x = event.rawX.toInt()
        val y = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mScroller?.isFinished == false) {
                    mScroller?.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY

                // 滑动
                scrollBy(-deltaX, 0)
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker?.computeCurrentVelocity(1000)
                var velocityX: Float = mVelocityTracker?.xVelocity ?: 0f // 水平方向的速度
                Log.i(TAG, "onTouchEvent: velocityX=$velocityX")

                mChildIndex = if (velocityX > 0) mChildIndex - 1 else mChildIndex + 1
                mChildIndex = max(0, min(mChildIndex, childCount - 1))

                var totalScroll = 0
                for (i: Int in 1..mChildIndex) {
                    var child = getChildAt(i)
                    var layoutParams = child.layoutParams as MarginLayoutParams
                    totalScroll += child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                }

                Log.i(TAG, "onTouchEvent: totalScroll=$totalScroll;scrollX=$scrollX")

                var dx = totalScroll - scrollX
                smoothScrollBy(dx, 0)

                mVelocityTracker?.clear()
            }
        }

        mLastX = x
        mLastY = y

        return super.onTouchEvent(event)

    }

    private fun smoothScrollBy(dx: Int, dy: Int) {
        mScroller?.startScroll(scrollX, scrollY, dx, dy)
        invalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller?.computeScrollOffset() == true) {
            var x = mScroller?.currX ?: scrollX
            var y = mScroller?.currY ?: scrollY
            scrollTo(x, y)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker?.recycle()
        super.onDetachedFromWindow()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        var handled= super.dispatchTouchEvent(ev)
        // getView()
        return handled
    }

    private fun getView() {
        try {
            val aClass: Class<*>? = javaClass.superclass
            val mFirstTouchTarget: Field = aClass!!.getDeclaredField("mFirstTouchTarget")
            Log.i(TAG, "getView: mFirstTouchTargetField=$mFirstTouchTarget")
            mFirstTouchTarget.isAccessible = true
            val o: Any = mFirstTouchTarget.get(this)
            Log.i(TAG, "getView: mFirstTouchTarget=$o")
            val targetClass: Class<*> = o.javaClass
            val childField: Field = targetClass.getDeclaredField("child")
            childField.isAccessible = true
            Log.i(TAG, "getView: childField=$childField")
            val targetView: Any = childField.get(o)
            Log.i(TAG, "getView: targetView=$targetView")
            val nextField: Field = targetClass.getDeclaredField("next")
            nextField.isAccessible = true
            Log.i(TAG, "getView: next=" + nextField.get(o))
            val groupFlagsField: Field = aClass.getDeclaredField("mGroupFlags")
            groupFlagsField.isAccessible = true
            val groupFlag = groupFlagsField.get(this) as Int
            val intercept = groupFlag and 0x80000 != 0
            Log.i(TAG, "getView: mGroupFlags=" + groupFlag + ";intercept="
                    + intercept)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}