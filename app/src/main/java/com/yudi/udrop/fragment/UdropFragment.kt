package com.yudi.udrop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yudi.udrop.R
import com.yudi.udrop.adapter.MessageAdapter
import com.yudi.udrop.model.data.MessageModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UdropFragment : Fragment() {

    private val adapter: MessageAdapter = MessageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_udrop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(view)
            .load(R.drawable.siri)
            .into(view.findViewById(R.id.udrop_microphone_gif))
        setupRecyclerView(view)
        val TimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        adapter.updateContent(
            0,
            MessageModel(false, "你好，我是语滴。", "${TimeFormatter.format(LocalDateTime.now())}")
        )
        adapter.updateContent(
            1,
            MessageModel(
                false,
                "你可以和我聊天，也可以输入“背书”，开始背书之旅哦~",
                "${TimeFormatter.format(LocalDateTime.now())}"
            )
        )
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.udrop_content)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }
}