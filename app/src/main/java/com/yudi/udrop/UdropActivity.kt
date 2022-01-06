package com.yudi.udrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yudi.udrop.adapter.MessageAdapter
import com.yudi.udrop.databinding.ActivityUdropBinding
import com.yudi.udrop.model.data.ToolbarModel

class UdropActivity : AppCompatActivity() {
    lateinit var binding: ActivityUdropBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udrop)
        Glide.with(this)
            .load(R.drawable.siri)
            .into(findViewById(R.id.udrop_microphone_gif))
        // TODO: get title
        binding.toolbarModel = ToolbarModel("语滴") // Test mock data
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.udrop_content)
        recyclerView.adapter = MessageAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }
}