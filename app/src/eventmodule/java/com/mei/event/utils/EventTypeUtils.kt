package com.mei.event.utils

import android.view.MotionEvent

/**
 * @date 2021/1/6
 * @author mxb
 * @desc
 * @desired
 */
class EventTypeUtils {

    companion object {

        fun getEventType(event: MotionEvent): String {
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
                MotionEvent.ACTION_UP -> "ACTION_UP"
                MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
                MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
                else -> event.action.toString()
            }
        }
    }


}