package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivityLoginBinding
import com.yudi.udrop.interfaces.InputInterface

class LoginActivity : AppCompatActivity(), InputInterface {
    lateinit var binding: ActivityLoginBinding
    private val SQLiteManager = SQLiteManager(this, "udrop.db", null, 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginPasswordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.password = s.toString()
            }

        })
        binding.toRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            super.finish()
        }
        binding.loginButton.setOnClickListener {
            val name = binding.loginNameEdit.text.toString()
            binding.password?.let {
                ServiceManager().login(name, it) { userId ->
                    when (userId) {
                        -2 -> Snackbar.make(binding.root, "登陆失败", Snackbar.LENGTH_SHORT).show()
                        -1 -> Snackbar.make(
                            binding.root,
                            "请检查用户名是否存在或密码是否正确",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        else -> {
                            SQLiteManager.addUser(userId, name)
                            startActivity(Intent(this, OverviewActivity::class.java))
                            super.finish()
                        }
                    }
                }
            } ?: run {
                Snackbar.make(binding.root, "请输入密码", Snackbar.LENGTH_SHORT).show()
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