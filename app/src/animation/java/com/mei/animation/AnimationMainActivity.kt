package com.mei.animation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mei.cuswidget.R

/**
 * @date 2021/1/9
 * @author mxb
 * @desc
 * @desired
 */
class AnimationMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_main)
    }

    fun simpleViewAnimation(view: View) {
        startActivity(Intent(this, ViewAnimationActivity::class.java))
    }

    fun layoutAnimation(view: View) {
        startActivity(Intent(this, LayoutAnimationActivity::class.java))
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim)
    }

    fun propertyAnimation(view: View) {
        startActivity(Intent(this, PropertyAnimationActivity::class.java))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim)
    }
}