package com.yudi.udrop

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //实现点击页面跳转
        findViewById<Button>(R.id.login_btn).setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        findViewById<Button>(R.id.register_btn).setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}