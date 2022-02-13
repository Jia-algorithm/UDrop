package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.databinding.ActivityRegisterBinding
import com.yudi.udrop.interfaces.InputInterface
import com.yudi.udrop.model.data.RegisterModel

class RegisterActivity : AppCompatActivity(), InputInterface {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.model = RegisterModel("", "")
        val SQLiteManager = SQLiteManager(this, "udrop.db", null, 1)
        binding.toLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            super.finish()
        }
        binding.registerPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (binding.model as RegisterModel).showInputWarning.set(true)
            }

            override fun afterTextChanged(s: Editable?) {
                (binding.model as RegisterModel).password = s.toString()
            }
        })
        binding.registerConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                (binding.model as RegisterModel).confirm = s.toString()
            }
        })
        binding.registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.register_name).text
            binding.model?.let { model ->
                if (model.tipIcon == R.drawable.ic_success && !model.showConfirmWarning && name.toString() != "") {
                    SQLiteManager.addUser(0, name.toString())
                    startActivity(Intent(this, OverviewActivity::class.java))
                    super.finish()
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN && currentFocus != null) {
            if (shouldHideInput(currentFocus!!, ev))
                hideKeyboard(this, currentFocus!!)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun finish() {
        startActivity(Intent(this, LaunchActivity::class.java))
        super.finish()
    }
}