package com.mei.event

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.mei.cuswidget.R

/**
 * @date 2021/1/5
 * @author mxb
 * @desc 滑动冲突场景1-外部拦截
 * @desired
 */
class DemoActivity1 : AppCompatActivity() {

    var mListView1: ListView? = null
    var mListView2: ListView? = null
    var mListView3: ListView? = null
    var mListView4: ListView? = null
    var mListView5: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo1)
        initView()
    }

    private fun initView() {
        mListView1 = findViewById(R.id.list_view1)
        mListView2 = findViewById(R.id.list_view2)
        mListView3 = findViewById(R.id.list_view3)
        mListView4 = findViewById(R.id.list_view4)
        mListView5 = findViewById(R.id.list_view5)
        // 创建一个ListView数组
        var listArray: Array<ListView?> = arrayOf(mListView1, mListView2, mListView3, mListView4, mListView5)

        var arrayAdapter: ArrayAdapter<String>
        for (i: Int in 0..4) {
            arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
            var dataList = ArrayList<String>()
            for (j: Int in 0..50) {
                dataList.add("第${i + 1}屏，第${j + 1}个元素")
            }
            arrayAdapter.addAll(dataList)
            listArray[i]!!.adapter = arrayAdapter
        }
    }
}