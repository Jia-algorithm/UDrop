package com.yudi.udrop

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.yudi.udrop.model.local.FunctionType
import com.yudi.udrop.util.InFileStream
import org.json.JSONObject
import java.util.*

open class WakeupActivity : AppCompatActivity(), EventListener {
    lateinit var wakeUp: EventManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
        wakeUp = EventManagerFactory.create(this, "wp")
        wakeUp.registerListener(this)
        val params: MutableMap<String?, Any?> = TreeMap()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
        params[SpeechConstant.APP_KEY] = "3kDBAGKK7pF6EL8a7s2Kgbog"
        params[SpeechConstant.APP_ID] = "25693846"
        params[SpeechConstant.SECRET] = "Wb3RGnlT17WBNMcC5qeCxOkLDL1cq6e3"
        InFileStream.setContext(this)
        wakeUp.send(SpeechConstant.WAKEUP_START, JSONObject(params).toString(), null, 0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeUp.send(SpeechConstant.WAKEUP_STOP, "{}", null, 0, 0)
    }

    override fun onEvent(
        name: String?,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        if (params != null && params.isNotEmpty()) {
            try {
                val response = JSONObject(params)
                if (response.getString("word").contains("语滴")) {
                    startActivity(Intent(this, UdropActivity::class.java).apply {
                        putExtra(UdropActivity.INTENT_EXTRA_TYPE, FunctionType.DEFAULT)
                        putExtra(UdropActivity.INTENT_EXTRA_TITLE, "语滴")
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (toApplyList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }
}