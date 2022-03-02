package com.yudi.udrop

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivityUdropBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.model.local.FunctionType
import org.json.JSONObject
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.set

class UdropActivity : AppCompatActivity(), ToolbarInterface, EventListener {
    lateinit var binding: ActivityUdropBinding
    lateinit var localManager: SQLiteManager
    private var userId = 2
    private val title: String by lazy {
        intent.getStringExtra(INTENT_EXTRA_TITLE) ?: "语滴"
    }
    private val functionType: FunctionType by lazy {
        intent.getStringExtra(INTENT_EXTRA_TYPE)?.let { FunctionType.typeFromId(it) }
            ?: FunctionType.DEFAULT
    }
    private val asr by lazy {
        EventManagerFactory.create(this, "asr")
    }
    private var speechSynthesizer: SpeechSynthesizer? = null

    private var running = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localManager = SQLiteManager(this, "udrop.db", null, 1)
        localManager.getInfo()?.let {
            userId = it.id
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udrop)
        binding.toolbarModel = ToolbarModel(title, R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        binding.result = ""
        binding.yourReply = ""
        initPermission()
        initTTS()
        asr.registerListener(this)
        binding.udropMicrophoneIcon.setOnClickListener {
            if (running) {
                Glide.with(this).clear(binding.udropSpeakingGif)
                stopListen()
                running = false
            } else {
                binding.yourReply = ""
                binding.result = ""
                Glide.with(this).asGif().load(R.drawable.ic_wave).into(binding.udropSpeakingGif)
                stopSpeak()
                startListen()
                running = true
            }
        }
        startCommunication { reply ->
            Handler(Looper.getMainLooper()).post {
                binding.result = "语滴：$reply"
                if (reply.length > 20)
                    reply.split("，", "。", "？", "！").forEach {
                        speak(it)
                    }
                else
                    speak(reply)
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
        if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) {
            params?.let { s ->
                if (s.contains("\"final_result\"")) {
                    Glide.with(this).clear(binding.udropSpeakingGif)
                    running = false
                    val result = JSONObject(s).getString("best_result")
                    binding.yourReply = "你：$result"
                    continueCommunication(result) { reply ->
                        Handler(Looper.getMainLooper()).post {
                            binding.result = "语滴：$reply"
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
        }
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

    private fun startListen() {
        val params = LinkedHashMap<String, Any>()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.PID] = 1537
        asr.send(SpeechConstant.ASR_START, JSONObject(params as Map<*, *>).toString(), null, 0, 0)
    }

    private fun stopListen() {
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

    private fun startCommunication(completion: (String) -> Unit) {
        when (functionType) {
            FunctionType.REVIEW -> {
                if (localManager.countUndoneReviewSchedule() == 0)
                    completion("今天没有要复习的故事")
                else
                    ServiceManager().communicate(userId, "复习") { _, reply ->
                        completion("现在是复习环节，$reply")
                    }
            }

            FunctionType.LEARN_NEW -> {
                if (localManager.countUndoneNewSchedule() == 0)
                    completion("今天的学习计划都完成啦")
                else
                    ServiceManager().communicate(userId, "学习") { _, reply ->
                        completion("现在是新学环节，$reply")
                    }
            }
            FunctionType.RECITE_WHOLE -> ServiceManager().communicate(userId, title) { _, _ ->
                ServiceManager().communicate(userId, "全文背诵") { _, reply ->
                    completion("你已进入全文背诵环节," + reply.substringAfter("好的"))
                }
            }
            FunctionType.RECITE_BY_SENTENCE -> ServiceManager().communicate(userId, title) { _, _ ->
                ServiceManager().communicate(userId, "逐句跟背") { _, reply ->
                    completion("你已进入逐句跟背环节," + reply.substringAfter("好的，"))
                }
            }
            FunctionType.GAME -> ServiceManager().communicate(userId, "游戏") { _, reply ->
                completion("让我们开始游戏吧。$reply")
            }
        }
    }

    private fun continueCommunication(text: String, completion: (String) -> Unit) {
        ServiceManager().communicate(userId, text) { _, reply ->
            completion(reply)
        }
    }

    companion object {
        const val TAG = "UDropActivity"
        const val INTENT_EXTRA_TITLE = "title"
        const val INTENT_EXTRA_TYPE = "function_type"
        const val appId = "25674272"
        const val appKey = "2U8zG0L8TidRMdL31HzG75FO"
        const val secretKey = "WqdU5HRGEnXdwQWfYIMzYQUFjGZiHGPW"
    }
}