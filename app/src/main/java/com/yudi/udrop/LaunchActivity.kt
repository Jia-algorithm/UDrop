package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yudi.udrop.data.SQLiteManager

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val SQLiteManager = SQLiteManager(this, "udrop.db", null, 1)
        SQLiteManager.getInfo()?.let {
            startActivity(Intent(this, OverviewActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_launch)
        //实现点击页面跳转
        findViewById<TextView>(R.id.launch_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        findViewById<TextView>(R.id.launch_register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}