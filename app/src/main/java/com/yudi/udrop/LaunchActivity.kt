package com.yudi.udrop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        //实现点击页面跳转
        findViewById<Button>(R.id.login_btn).setOnClickListener {
            startActivity(Intent(this,OverviewActivity::class.java))
        }
        findViewById<Button>(R.id.register_btn).setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}