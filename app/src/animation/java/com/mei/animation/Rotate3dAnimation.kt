package com.mei.animation

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * @date 2021/1/9
 * @author mxb
 * @desc Android 的ApiDemo中的一个自定义View动画
 * 可以围绕y轴旋转并且同时沿着z轴平移，从而实现一种类似3D的效果
 * @desired
 */
class Rotate3dAnimation(private val mFromDegrees: Float,
                        private val mToDegrees: Float,
                        private val mCenterX: Float,
                        private val mCenterY: Float,
                        private val mDepthZ: Float,
                        private val mReverse: Boolean) : Animation() {

    private var mCamera: Camera? = null


    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        mCamera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val fromDegrees = mFromDegrees
        var degree = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime

        val centerX = mCenterX
        val centerY = mCenterY

        val camera = mCamera

        val matrix = t?.matrix

        camera?.save()
        if (mReverse) {
            camera?.translate(0f, 0f, mDepthZ * interpolatedTime)
        } else {
            camera?.translate(0f, 0f, mDepthZ * (1f - interpolatedTime))
        }
        camera?.rotateY(degree)
        camera?.getMatrix(matrix)

        camera?.restore()

        matrix?.preTranslate(-centerX, -centerY)
        matrix?.postTranslate(centerX, centerY)
    }

}