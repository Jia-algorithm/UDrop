package com.yudi.udrop

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.yudi.udrop.databinding.ActivityUdropBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.util.AutoCheck
import org.json.JSONObject
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.Map
import kotlin.collections.set

class UdropActivity : AppCompatActivity(), ToolbarInterface, EventListener {
    lateinit var binding: ActivityUdropBinding
    private val titleResId: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.app_name_cn)
    }
    private val asr by lazy {
        EventManagerFactory.create(this, "asr")
    }
    protected var enableOffline = false
    private var running = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udrop)
        binding.toolbarModel = ToolbarModel(getString(titleResId), R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        initPermission()
        asr.registerListener(this)
        binding.udropMicrophoneIcon.setOnClickListener {
            if (running) {
                stop()
                running = false
            } else {
                start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
        asr.unregisterListener(this)
    }

    private fun start() {
        val params = LinkedHashMap<String, Any>()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.PID] = 1537

        AutoCheck(
            applicationContext, @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    if (msg.what == 100) {
                        val autoCheck: AutoCheck = msg.obj as AutoCheck
                        synchronized(autoCheck) {
                            val message: String =
                                autoCheck.obtainErrorMessage() // autoCheck.obtainAllMessage();
                            Log.i(TAG, message)
                        }
                    }
                }
            }, enableOffline
        ).checkAsr(params)
        asr.send(SpeechConstant.ASR_START, JSONObject(params as Map<*, *>).toString(), null, 0, 0)
    }

    private fun stop() {
        Log.i(TAG, "ASR STOP")
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0)
    }

    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val toApplyList = ArrayList<String>()
        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    this,
                    perm
                )
            ) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                toApplyList.toArray(tmpList),
                123
            )
        }
    }

    companion object {
        const val TAG = "UDropActivity"
        const val INTENT_EXTRA_TITLE = "title"
    }

    override fun onLeftItemClick() {
        onBackPressed()
    }

    override fun onEvent(
        name: String?,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        var logTxt = "name: $name"

        if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) {
            // 识别相关的结果都在这里
            if (params == null || params.isEmpty()) {
                return
            }
            if (params.contains("\"nlu_result\"")) {
                // 一句话的语义解析结果
                if (data != null && length > 0 && data.size > 0)
                    logTxt += ", 语义解析结果：" + String(data, offset, length)
            } else if (params.contains("\"partial_result\"")) {
                // 一句话的临时识别结果
                logTxt += ", 临时识别结果：$params"
            } else if (params.contains("\"final_result\"")) {
                // 一句话的最终识别结果
                logTxt += ", 最终识别结果：$params"
            } else {
                // 一般这里不会运行
                logTxt += " ;params :$params"
                if (data != null) {
                    logTxt += " ;data length=" + data.size
                }
            }
        } else {
            // 识别开始，结束，音量，音频数据回调
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :$params"
            }
            if (data != null) {
                logTxt += " ;data length=" + data.size
            }
        }
        Log.i(TAG, logTxt)
        binding.udropResultText.text = logTxt
    }
}