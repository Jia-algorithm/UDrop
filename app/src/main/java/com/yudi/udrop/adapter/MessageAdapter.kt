package com.yudi.udrop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.UdropMessageItemBinding
import com.yudi.udrop.model.data.MessageModel

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: UdropMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var messageList: ArrayList<MessageModel> = arrayListOf()

    fun updateContent(position: Int, message: MessageModel) {
        messageList.add(message)
        notifyItemChanged(position)
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
        holder.binding.model = messageList[position]
    }

    override fun getItemCount(): Int = messageList.size
}