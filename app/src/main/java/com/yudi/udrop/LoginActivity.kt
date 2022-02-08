package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yudi.udrop.interfaces.InputInterface

class LoginActivity : AppCompatActivity(), InputInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<TextView>(R.id.to_register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        findViewById<Button>(R.id.login_button).setOnClickListener {
            startActivity(Intent(this, OverviewActivity::class.java))
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN && currentFocus != null) {
            if (shouldHideInput(currentFocus!!, ev))
                hideKeyboard(this, currentFocus!!)
        }
        return super.dispatchTouchEvent(ev)
    }
}