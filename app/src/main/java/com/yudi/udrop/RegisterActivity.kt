package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViewById<TextView>(R.id.to_login).setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}