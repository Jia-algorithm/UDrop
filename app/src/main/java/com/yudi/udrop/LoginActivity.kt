package com.yudi.udrop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<TextView>(R.id.to_register).setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}