package com.mei.animation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mei.cuswidget.R

/**
 * @date 2021/1/9
 * @author mxb
 * @desc
 * @desired
 */
class ViewAnimationActivity : AppCompatActivity() {

    private var mBtnAnimation: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation)

        mBtnAnimation = findViewById(R.id.btn_animation)

        mBtnAnimation?.setOnClickListener(View.OnClickListener {
            // var animation = AnimationUtils.loadAnimation(this, R.anim.view_animation)
            // mBtnAnimation?.startAnimation(animation)

            var animation = Rotate3dAnimation(0f, 180f, 100f, 1000f, 100f, false)
            animation.duration = 4000
            mBtnAnimation?.startAnimation(animation)
        })
    }
}