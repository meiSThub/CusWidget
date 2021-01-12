package com.mei.cuswidget;

import com.mei.animation.AnimationMainActivity;
import com.mei.event.EventMainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void viewEvent(View view) {
        startActivity(new Intent(this, EventMainActivity.class));
    }

    public void animation(View view) {
        startActivity(new Intent(this, AnimationMainActivity.class));
    }
    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev) {
    //     Log.i("ScrollSelfView", "MainActivity dispatchTouchEvent: ----" + ev.getAction());
    //     return super.dispatchTouchEvent(ev);
    // }
    //
    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    //     Log.i("ScrollSelfView", "MainActivity onTouchEvent: ----" + event.getAction());
    //     return super.onTouchEvent(event);
    // }
}