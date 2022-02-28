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
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.TtsMode
import com.bumptech.glide.Glide
import com.yudi.udrop.databinding.ActivityUdropBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.util.AutoCheck
import org.json.JSONObject
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.set

class UdropActivity : AppCompatActivity(), ToolbarInterface, EventListener {
    lateinit var binding: ActivityUdropBinding
    private val titleResId: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.app_name_cn)
    }
    private val asr by lazy {
        EventManagerFactory.create(this, "asr")
    }
    private var speechSynthesizer: SpeechSynthesizer? = null

    private var running = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udrop)
        binding.toolbarModel = ToolbarModel(getString(titleResId), R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        binding.result = "你好，我是语滴，点击图标和我说话吧！"
        initPermission()
        initTTS()
        asr.registerListener(this)
        binding.udropMicrophoneIcon.setOnClickListener {
            if (running) {
                stop()
                running = false
            } else {
                binding.result = ""
                Glide.with(this).asGif().load(R.drawable.siri).into(binding.udropSpeakingGif)
                stopSpeak()
                start()
            }
        }
    }

    override fun onPause() {
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
        super.onPause()
    }

    override fun onDestroy() {
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
        asr.unregisterListener(this)
        speechSynthesizer?.let {
            it.stop()
            it.release()
        }
        speechSynthesizer = null
        super.onDestroy()
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
        var logTxt = ""

        if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) {
            params?.let {
                if (it.contains("\"final_result\"")) {
                    Glide.with(this).clear(binding.udropSpeakingGif)
                    binding.result = JSONObject(it).getString("best_result")
                }
            }
        } else {
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :$params"
            }
            if (data != null) {
                logTxt += " ;data length=" + data.size
            }
        }
        Log.i(TAG, logTxt)
    }

    private fun initTTS() {
        speechSynthesizer = SpeechSynthesizer.getInstance()
        speechSynthesizer?.let {
            it.setContext(this)
            var result = it.setAppId(appId)
            checkResult(result, "setAppId")
            result = it.setApiKey(appKey, secretKey)
            checkResult(result, "setApiKey")
            it.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0")
            it.setParam(SpeechSynthesizer.PARAM_VOLUME, "9")
            it.setParam(SpeechSynthesizer.PARAM_SPEED, "5")
            it.setParam(SpeechSynthesizer.PARAM_PITCH, "5")
            result = it.initTts(TtsMode.ONLINE)
            checkResult(result, "initTts")
        }
        binding.result?.let {
            speak(it)
        }
    }

    private fun speak(text: String) {
        speechSynthesizer?.let {
            val result = it.speak(text)
            checkResult(result, "speak")
        } ?: run { Log.e(TAG, "[ERROR], 初始化失败") }
    }

    private fun stopSpeak() {
        speechSynthesizer?.let {
            val result = it.stop()
            checkResult(result, "stop speak")
        }
    }

    private fun checkResult(result: Int, method: String) {
        if (result != 0) Log.e(TAG, "error code:$result method:$method")
    }

    private fun start() {
        val params = LinkedHashMap<String, Any>()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.PID] = 1537
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
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
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
        const val appId = "25674272"
        const val appKey = "2U8zG0L8TidRMdL31HzG75FO"
        const val secretKey = "WqdU5HRGEnXdwQWfYIMzYQUFjGZiHGPW"
    }
}