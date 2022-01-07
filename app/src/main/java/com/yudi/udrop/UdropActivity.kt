package com.yudi.udrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yudi.udrop.adapter.MessageAdapter
import com.yudi.udrop.databinding.ActivityUdropBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel

class UdropActivity : AppCompatActivity(), ToolbarInterface {
    lateinit var binding: ActivityUdropBinding
    private val titleResId: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.app_name_cn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udrop)
        Glide.with(this)
            .load(R.drawable.siri)
            .into(findViewById(R.id.udrop_microphone_gif))
        binding.toolbarModel = ToolbarModel(getString(titleResId), R.drawable.ic_back_to_home)
        binding.toolbarHandler = this
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.udrop_content)
        recyclerView.adapter = MessageAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }

    companion object {
        const val INTENT_EXTRA_TITLE = "title"
    }

    override fun onLeftItemClick() {
        onBackPressed()
    }
}