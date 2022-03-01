package com.yudi.udrop.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.TtsMode
import com.bumptech.glide.Glide
import com.yudi.udrop.R
import com.yudi.udrop.UdropActivity
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.FragmentUdropBinding
import org.json.JSONObject
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.set

class UdropFragment : Fragment(), EventListener {
    lateinit var binding: FragmentUdropBinding
    lateinit var localManager: SQLiteManager
    private val asr by lazy {
        EventManagerFactory.create(context, "asr")
    }
    private var speechSynthesizer: SpeechSynthesizer? = null
    private var userId = 2
    private var running = false

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentUdropBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localManager = SQLiteManager(context, "udrop.db", null, 1)
        localManager.getInfo()?.let {
            userId = it.id
        }
        binding.result = "你好，我是语滴，点击图标和我说话吧！"
        initPermission()
        initTTS()
        asr.registerListener(this)
        binding.udropMicrophoneIcon.setOnClickListener {
            if (running) {
                Glide.with(this).clear(binding.udropSpeakingGif)
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
        ServiceManager().communicate(userId, "结束") { _, _ -> }
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
        ServiceManager().communicate(userId, "结束") { _, _ -> }
        super.onDestroy()
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
                    val result = JSONObject(it).getString("best_result")
                    continueCommunication(result) { reply ->
                        Handler(Looper.getMainLooper()).post {
                            binding.result = reply
                            if (reply.length > 20)
                                reply.split("，", "。", "？", "！").forEach {
                                    speak(it)
                                }
                            else
                                speak(reply)
                        }
                    }
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
        Log.i(UdropActivity.TAG, logTxt)
    }

    private fun initTTS() {
        speechSynthesizer = SpeechSynthesizer.getInstance()
        speechSynthesizer?.let {
            it.setContext(context)
            var result = it.setAppId(UdropActivity.appId)
            checkResult(result, "setAppId")
            result = it.setApiKey(UdropActivity.appKey, UdropActivity.secretKey)
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
            if (PackageManager.PERMISSION_GRANTED != activity?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        perm
                    )
                }
            ) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    toApplyList.toArray(tmpList),
                    123
                )
            }
        }
    }

    private fun continueCommunication(text: String, completion: (String) -> Unit) {
        ServiceManager().communicate(userId, text) { _, reply ->
            completion(reply)
        }
    }

    companion object {
        const val TAG = "UDropFragment"
    }
}