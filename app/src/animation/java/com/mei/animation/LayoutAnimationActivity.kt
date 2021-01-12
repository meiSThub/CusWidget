package com.mei.animation

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.mei.cuswidget.R

/**
 * @date 2021/1/9
 * @author mxb
 * @desc
 * @desired
 */
class LayoutAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_animation)

        var listView: ListView = findViewById(R.id.list_view)

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)

        var dataList = ArrayList<String>()
        for (i in 0..100) {
            dataList.add("item 编号：${i + 1}")
        }
        adapter.addAll(dataList)

        listView.adapter = adapter

        var animation = AnimationUtils.loadAnimation(this, R.anim.anim_item)
        var controller = LayoutAnimationController(animation)
        controller.delay = 0.5f
        controller.order = LayoutAnimationController.ORDER_NORMAL
        listView.layoutAnimation = controller


    }
}