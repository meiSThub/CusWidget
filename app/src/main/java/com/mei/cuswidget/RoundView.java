package com.mei.cuswidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * @author mxb
 * @date 2020/11/13
 * @desc
 * @desired
 */
public class RoundView extends FrameLayout {

    private static final String TAG = "RoundView";

    private Bitmap mBitmap;

    private Bitmap mSrcBitmap;

    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_credi_loan_item_shadow_only);

        mSrcBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mSrcBitmap);
        canvas.drawColor(Color.RED);

    }

    private Paint mPaint = new Paint();


    private Scroller mScroller = new Scroller(getContext());

    /**
     * 缓慢滚动到指定位置
     *
     * @param destX 目标位置x
     */
    private void smoothScroll(int destX, int destY) {
        Log.i(TAG, "smoothScroll: destX=" + destX + ";destY=" + destY);
        if (Math.abs(destX) < touchSlop && Math.abs(destY) < touchSlop) {
            return;
        }
        int scrollX = getScrollX();
        Log.i(TAG, "smoothScroll: scrollX=" + scrollX);
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private int startY;

    private int startX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                // smoothScroll(disX, disY);
                break;
        }
        startX = x;
        startY = y;
        return true;
    }

    // @Override
    // protected void onDraw(Canvas canvas) {
    //     super.onDraw(canvas);
    //
    //     int save = canvas.save();
    //     canvas.translate(100, 100);
    //     canvas.translate(20, 20);
    //     canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint);
    //     mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    //     canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    //     mPaint.setXfermode(null);
    //     canvas.restoreToCount(save);
    // }

    private int dp2px(int dipValue) {
        if (dipValue == 0) {
            return 0;
        }
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
