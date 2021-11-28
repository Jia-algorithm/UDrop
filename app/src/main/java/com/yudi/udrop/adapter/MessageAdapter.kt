package com.yudi.udrop.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.UdropMessageItemBinding
import com.yudi.udrop.model.data.MessageModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessageAdapter(val context: Context) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: UdropMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var messageList : ArrayList<MessageModel>

    private fun getData() {
        var newMessage : ArrayList<MessageModel> = ArrayList()
        val TimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        newMessage.add(MessageModel(false, "你好，我是语滴。", "${TimeFormatter.format(LocalDateTime.now())}"))
        newMessage.add(MessageModel(false, "你可以和我聊天，也可以输入“背书”，开始背书之旅哦~", "${TimeFormatter.format(LocalDateTime.now())}"))
        messageList = newMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<UdropMessageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.udrop_message_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getData()
        holder.binding.model = messageList[position]
//        val TimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
//        holder.binding.model = MessageModel(false,"你好，我是语滴！","${TimeFormatter.format(LocalDateTime.now())}")
    }

    override fun getItemCount(): Int = 2
}