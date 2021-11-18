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

    private var messageList = mutableListOf<MessageModel>()

    private fun getData() {
        val time = System.currentTimeMillis()
        val format: CharSequence =
            DateFormat.format("yyyy" + "/" + "MM" + "/" + "dd" + " " + "hh:mm:ss", time)
        messageList.add(MessageModel(false, "你好，我是语滴~", format.toString()))
        messageList.add(MessageModel(false, "每天都要坚持背书哦~", format.toString()))
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
//        getData()
//        holder.binding.model = messageList[position]
        val TimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        holder.binding.model = MessageModel(false,"你好，我是语滴！","${TimeFormatter.format(LocalDateTime.now())}")
    }

    override fun getItemCount(): Int = 1
}