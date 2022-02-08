package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.interfaces.InputInterface

class RegisterActivity : AppCompatActivity(), InputInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val SQLiteManager = SQLiteManager(this, "udrop.db", null, 1)
        setContentView(R.layout.activity_register)
        findViewById<TextView>(R.id.to_login).setOnClickListener {
            SQLiteManager.addUser(0, "", "", 0)
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // TODO: check input
        findViewById<Button>(R.id.register_button).setOnClickListener {
            val name = findViewById<EditText>(R.id.register_name).text // TODO: use viewmodel?
            val password = findViewById<EditText>(R.id.register_password).text
            SQLiteManager.addUser(0, name.toString(), "", 0)
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