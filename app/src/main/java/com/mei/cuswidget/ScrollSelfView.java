package com.mei.cuswidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import androidx.annotation.Nullable;

import static com.mei.cuswidget.MyButton.getEventType;

/**
 * @author mxb
 * @date 2020/12/20
 * @desc
 * @desired
 */
public class ScrollSelfView extends ViewGroup {

    private static final String TAG = "ScrollSelfView";

    private int startY;

    private int startX;

    public ScrollSelfView(Context context) {
        super(context);
    }

    public ScrollSelfView(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollSelfView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.layout(l, t, r, 500);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "ScrollSelfView onInterceptTouchEvent: ----" + getEventType(ev));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                getView();
                return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "ScrollSelfView onTouchEvent: ---" + getEventType(event));
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = x - startX;
                int disY = y - startY;
                setTranslationX(getTranslationX() + disX);
                setTranslationY(getTranslationY() + disY);
                break;
        }
        startX = x;
        startY = y;
        return false;
    }

    private void getView() {
        try {
            Class aClass = getClass().getSuperclass();
            Field mFirstTouchTarget = aClass.getDeclaredField("mFirstTouchTarget");
            Log.i(TAG, "ScrollSelfView#getView: mFirstTouchTargetField=" + mFirstTouchTarget);
            mFirstTouchTarget.setAccessible(true);
            Object o = mFirstTouchTarget.get(this);
            Log.i(TAG, "ScrollSelfView#getView: mFirstTouchTarget=" + o);
            Class<?> targetClass = o.getClass();
            Field childField = targetClass.getDeclaredField("child");
            childField.setAccessible(true);
            Log.i(TAG, "ScrollSelfView#getView: childField=" + childField);
            Object targetView = childField.get(o);
            Log.i(TAG, "ScrollSelfView#getView: targetView=" + targetView);

            Field nextField = targetClass.getDeclaredField("next");
            nextField.setAccessible(true);
            Log.i(TAG, "ScrollSelfView#getView: next=" + nextField.get(o));

            Field groupFlagsField = aClass.getDeclaredField("mGroupFlags");
            groupFlagsField.setAccessible(true);
            int groupFlag = (int) groupFlagsField.get(this);
            boolean intercept = (groupFlag & 0x80000) == 0;
            Log.i(TAG, "ScrollSelfView#getView: mGroupFlags=" + groupFlagsField.get(this) + ";intercept="
                    + intercept);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


