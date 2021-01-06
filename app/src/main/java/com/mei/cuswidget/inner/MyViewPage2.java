package com.mei.cuswidget.inner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author mxb
 * @date 2020/12/20
 * @desc 内部拦截法，解决滑动冲突
 * @desired
 */
public class MyViewPage2 extends ViewGroup {

    private static final String TAG = "MyViewPage2";

    public MyViewPage2(Context context) {
        this(context, null);
    }

    public MyViewPage2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPage2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    private int mLastX, mLastY;

    private int mLastXIntercept, mLastYIntercept;

    private int mChildIndex;// 当前显示的View的下标

    private int mChildWidth;// 每个item的宽度

    private int mChildSize;// item数量

    private void init(Context context) {
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = x;
            mLastY = y;
            // 为了防止ViewPager左右滑动还没有完成的时候，就进行上下滑动，使得ViewPager停留在中间状态，
            // 而无法到达终点
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

}
