package com.yudi.udrop

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.yudi.udrop.adapter.MessageAdapter
import com.yudi.udrop.databinding.ActivityUdropBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel

class UdropActivity : AppCompatActivity(), ToolbarInterface, EventListener {
    lateinit var binding: ActivityUdropBinding
    private val titleResId: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.app_name_cn)
    }

    private val asr: EventManager by lazy {
        EventManagerFactory.create(this, "asr")
    }
    private val permissionsAuthorized: Boolean
        get() = (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udrop)
        binding.toolbarModel = ToolbarModel(getString(titleResId), R.drawable.ic_back_to_home)
        binding.toolbarHandler = this
        binding.color = R.color.white
        setupRecyclerView()
        initPermission()
        asr.registerListener(this)
        binding.udropContainer.udropMicrophone.setOnClickListener {
            if (binding.color == R.color.white) {
                binding.color = R.color.black_blue
                start()
            } else {
                binding.color = R.color.white
                stop()
            }

        }
    }

    private fun start() {
        asr.send(SpeechConstant.ASR_START, ASR_START_PARAM, null, 0, 0)
        Log.i(TAG, "ASR start.")
    }

    private fun stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0)
        Log.i(TAG, "ASR stop.")
    }

    override fun onEvent(
        name: String,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL
            && !params.isNullOrEmpty()
            && params.contains("\"final_result\"")
        ) {
            Log.i(TAG, "return result: $params")
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.udrop_content)
        recyclerView.adapter = MessageAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }

    companion object {
        const val TAG = "UDropActivity"
        const val INTENT_EXTRA_TITLE = "title"
        const val ALLOW_PERMISSIONS = 1
        const val ASR_START_PARAM =
            "{\"appid\":15590577,\"secret\":\"y7S7hAI894BB3LF1yHYmvQEus1B6wPvj \",\"key\":\"q2uPyBe6LmWTZlvb0g1dzcHV\",\"accept-audio-volume\":false}"
    }

    override fun onLeftItemClick() {
        onBackPressed()
    }

    private fun initPermission() {
        if (!permissionsAuthorized)
            requestPermissions(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), ALLOW_PERMISSIONS
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionsAuthorized) {
            // TODO: start function
        } else {
            finish()
        }
    }
}