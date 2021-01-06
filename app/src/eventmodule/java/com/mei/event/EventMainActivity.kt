package com.mei.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mei.cuswidget.R

/**
 * View的事件体系入口类
 */
class EventMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_main)
    }

    fun onButtonClick(view: View) {
        when (view.id) {
            R.id.button1 -> {
                var intent = Intent(this, TestActivity::class.java)
                startActivity(intent)
            }

            R.id.button2 -> {
                var intent = Intent(this, DemoActivity1::class.java)
                startActivity(intent)
            }

            R.id.button3 -> {
                var intent = Intent(this, DemoActivity2::class.java)
                startActivity(intent)
            }
            else -> {

            }
        }
    }


}