package com.mei.cuswidget.sticky;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author mxb
 * @date 2020/12/27
 * @desc 场景二：同方向的滑动冲突解决，这里是垂直方向
 * 实现上下滑动的LinearLayout，在它内部方一个header和一个ListView，这样内外两层都能上下滑动
 * @desired
 */
public class StickyLayout extends LinearLayout {

    private static final String TAG = "StickyLayout";

    private static final int STATUS_EXPANDED = 0;

    private int mTouchSlop;

    private int mLastXIntercept, mLastYIntercept;

    private int mLastX, mLastY;

    private boolean mDisallowInterceptTouchEventOnHeader;

    private int mStatus;

    private GiveUpTouchEventListener mGiveUpTouchEventListener;

    private boolean mIsSticky;

    // 头部的高度
    private int mHeaderHeight;

    // 头部原始高度
    private int mOriginalHeaderHeight;

    public StickyLayout(Context context) {
        this(context, null);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public interface GiveUpTouchEventListener {

        boolean giveUpTouchEvent(MotionEvent event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastX = x;
                mLastY = y;
                intercepted = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (mDisallowInterceptTouchEventOnHeader && y <= getHeaderHeight()) {
                    intercepted = 0;// 事件落在头部时不拦截
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    intercepted = 0;// 说明是水平滑动，不拦截
                } else if (mStatus == STATUS_EXPANDED && deltaY <= -mTouchSlop) {
                    intercepted = 1;// 如果是展开状态，且向上滑动，则拦截事件
                } else if (mGiveUpTouchEventListener != null) {
                    if (mGiveUpTouchEventListener.giveUpTouchEvent(event) && deltaY >= mTouchSlop) {
                        intercepted = 1;// 如果内部滑动到顶部了，且向下滑动，则外层容器拦截事件，把header滑出来
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = 0;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            default:
                break;
        }
        Log.i(TAG, "onInterceptTouchEvent: intercepted=" + intercepted);
        return intercepted != 0 && mIsSticky;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;

                Log.i(TAG, "onTouchEvent: onHeaderHeight=" + mHeaderHeight + ";deltaY=" + deltaY
                        + ";mlastY=" + mLastY);
                mHeaderHeight += deltaY;
                setHeaderHeight(mHeaderHeight);
                break;
            case MotionEvent.ACTION_UP:
                // 这里做了一下判断，当松开手的时候，会自动向
                int destHeight = 0;
                if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
                    destHeight = 0;
                } else {
                    destHeight = mOriginalHeaderHeight;
                    mStatus = STATUS_EXPANDED;
                }
                smoothSetHeaderHeight(mHeaderHeight, destHeight, 500);

                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothSetHeaderHeight(int headerHeight, int destHeight, int i) {

    }

    private int getHeaderHeight() {
        return 0;
    }

    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
    }
}
