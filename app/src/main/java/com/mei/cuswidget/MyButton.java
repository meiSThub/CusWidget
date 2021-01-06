package com.mei.cuswidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author mxb
 * @date 2020/12/27
 * @desc
 * @desired
 */
public class MyButton extends View {

    private static final String TAG = "ScrollSelfView";

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, " MyView dispatchTouchEvent: event=" + getEventType(event));
        super.dispatchTouchEvent(event);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, " MyView onTouchEvent: event=" + getEventType(event));
        return true;
    }

    public static String getEventType(MotionEvent event) {
        String actionType;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionType = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                actionType = "ACTION_UP";
                break;
            case MotionEvent.ACTION_MOVE:
                actionType = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                actionType = "ACTION_CANCEL";
                break;
            default:
                actionType = String.valueOf(event.getAction());
                break;
        }
        return actionType;
    }
}
