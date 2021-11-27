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
        findViewById<Button>(R.id.launch_login).setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        findViewById<Button>(R.id.launch_register).setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}