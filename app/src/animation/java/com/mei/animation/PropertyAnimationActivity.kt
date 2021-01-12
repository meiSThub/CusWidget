package com.mei.animation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mei.cuswidget.R


/**
 * @date 2021/1/11
 * @author mxb
 * @desc
 * @desired
 */
class PropertyAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animation)
    }

    fun translate(view: View) {
        ObjectAnimator.ofFloat(view, "translationY", 0f, 100f).start()
    }

    fun changeBg(view: View) {
        var animator = ObjectAnimator.ofInt(view, "backgroundColor",
                Color.parseColor("#FFFF8080"), Color.parseColor("#FF8080FF"))
        animator.duration = 3000
        animator.setEvaluator(ArgbEvaluator())
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.start()
    }

    fun animatorSet(view: View) {

        var set = AnimatorSet()
        set.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", 0f, 360f),
                ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f),
                ObjectAnimator.ofFloat(view, "rotation", 0f, -90f),
                ObjectAnimator.ofFloat(view, "translationX", 0f, 90f),
                ObjectAnimator.ofFloat(view, "translationY", 0f, 90f),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0.25f, 1f)
        )
        set.duration = 5 * 1000
        set.start()
    }

    fun animatorXML(view: View) {
        val set = AnimatorInflater.loadAnimator(this, R.animator.property_animation) as AnimatorSet
        set.setTarget(view)
        set.start()
    }
}