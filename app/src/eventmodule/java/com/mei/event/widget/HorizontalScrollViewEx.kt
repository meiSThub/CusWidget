package com.mei.event.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @date 2021/1/5
 * @author mxb
 * @desc 滑动冲突，外部拦截法
 * @desired
 */
class HorizontalScrollViewEx : ViewGroup {

    private val TAG = "HorizontalScrollViewEx"

    private var mScroller: Scroller = Scroller(getContext())
    private var mLastInterceptX: Int = 0
    private var mLastInterceptY: Int = 0
    private var mLastX: Int = 0
    private var mLastY: Int = 0
    private var mVelocityTracker: VelocityTracker = VelocityTracker.obtain()

    // 子view的宽度
    private var mChildWidth: Int = 0

    // 当前滑动到的子View下标
    private var mChildIndex: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 1.测量所有的子View
        var maxHeight = 0
        var totalWidth = 0;
        for (i: Int in 0 until childCount) {
            var child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            measureChildWithMargins(child, widthMeasureSpec, paddingLeft + paddingRight, heightMeasureSpec, paddingTop + paddingBottom)
            // 获取高度最大的子view的高度
            maxHeight = max(maxHeight, child.measuredHeight)
            var layoutParams = child.layoutParams as MarginLayoutParams
            totalWidth += child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
        }

        // 2. 测量完子View之后，根据子View测量得到的宽高，计算自己的宽高
        // 保存测量后的宽高
        var measureWidth: Int
        var measureHeight: Int

        var widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        if (childCount == 0) {
            measureWidth = widthSpecSize
            measureHeight = heightSpecSize
        } else {
            // 计算宽度
            measureWidth = when (widthSpecMode) {
                MeasureSpec.AT_MOST -> totalWidth
                MeasureSpec.EXACTLY -> widthSpecSize
                else -> widthSpecSize
            }

            // 计算高度
            measureHeight = when (heightSpecMode) {
                MeasureSpec.AT_MOST -> maxHeight
                MeasureSpec.EXACTLY -> heightSpecSize
                else -> heightSpecSize
            }
        }

        Log.i(TAG, "onMeasure: measureWidth=$measureWidth;measureHeight=$measureHeight")
        // 设置测量后的宽高
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        for (i: Int in 0 until childCount) {
            var childView = getChildAt(i)
            if (childView.visibility == View.GONE) {
                continue
            }
            var params: MarginLayoutParams = childView.layoutParams as MarginLayoutParams
            left += params.leftMargin
            var right = min(left + childView.measuredWidth, measuredWidth)// 右边不能超过父容器
            var top = paddingTop + params.topMargin
            var bottom = min(top + childView.measuredHeight, measuredHeight - params.bottomMargin)// 底部不能超过父容器
            Log.i(TAG, "onLayout: left=$left top=$top right=$right bottom=$bottom")
            childView.layout(left, top, right, bottom)
            left = right + params.rightMargin
            mChildWidth = childView.width
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        var x = ev.rawX.toInt()
        var y = ev.rawY.toInt()

        when (ev.action) {

            MotionEvent.ACTION_DOWN -> if (!mScroller.isFinished) {
                mScroller.abortAnimation() // 如果左右滑动没有完成，则直接滑动到底
                intercept = true // 并且拦截事件
            }

            MotionEvent.ACTION_MOVE -> {// 滑动事件
                var deltaX = x - mLastInterceptX
                var deltaY = y - mLastInterceptY

                // 水平滑动的距离比垂直方向滑动的距离大，则拦截事件
                intercept = abs(deltaX) > abs(deltaY)
            }

            MotionEvent.ACTION_UP -> intercept = false

            else -> intercept = false
        }

        mLastInterceptX = x
        mLastInterceptY = y
        mLastX = x
        mLastY = y
        Log.i(TAG, "onInterceptTouchEvent: intercepted=$intercept")
        return intercept
    }

    private var totalScrollX: Int = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)

        var x = event.rawX.toInt()
        var y = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var deltaX = x - mLastX// 滑动的距离=终点-起点
                var deltaY = y - mLastY
                // Log.i(TAG, "onTouchEvent: deltaX=$deltaX;deltaY=$deltaY")
                totalScrollX += deltaX
                scrollBy(-deltaX, 0)
                Log.i(TAG, "onTouchEvent: scrollX=${scrollX};totalScrollX=${totalScrollX}")
            }

            MotionEvent.ACTION_UP -> { // 手指抬起时，计算滑动的速度

                var scrollX = scrollX

                // 计算1s钟的滑动速度
                mVelocityTracker.computeCurrentVelocity(1000)
                // 水平滑动的速度
                var velocityX = mVelocityTracker.xVelocity
                mChildIndex = if (abs(velocityX) >= 50) {
                    if (velocityX > 0) mChildIndex - 1 else mChildIndex + 1
                } else {
                    (scrollX + mChildWidth / 2) / mChildWidth
                }

                // 保证下标不越界
                mChildIndex = max(0, min(mChildIndex, childCount - 1))

                // 要想滑动到指定的子View，在手指抬起的时候，还需要滑动多少距离

                var totalWidth = 0
                for (i: Int in 1..mChildIndex) {
                    var child = getChildAt(i)
                    var layoutParams = child.layoutParams as MarginLayoutParams
                    totalWidth += child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                }
                var dx = totalWidth - scrollX

                // 滑动指定的距离
                smoothScrollBy(dx, 0)
                // 清除速度的计算
                mVelocityTracker.clear()
            }
        }

        mLastX = x
        mLastY = y

        return true
    }

    private fun smoothScrollBy(dx: Int, dy: Int) {
        mScroller.startScroll(scrollX, scrollY, dx, dy)
        invalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker.recycle()
        super.onDetachedFromWindow()
    }
}
