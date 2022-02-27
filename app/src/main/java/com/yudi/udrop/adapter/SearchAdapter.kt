package com.yudi.udrop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.NoDataItemBinding
import com.yudi.udrop.databinding.SearchTextItemBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.model.local.TextDetail

class SearchAdapter(private val handler: ProgressInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(val binding: SearchTextItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DummyHolder(val binding: NoDataItemBinding) : RecyclerView.ViewHolder(binding.root)

    var resultList: ArrayList<TextDetail> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = DataBindingUtil.inflate<NoDataItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.no_data_item,
                    parent,
                    false
                )
                DummyHolder(binding)
            }
            1 -> {
                val binding = DataBindingUtil.inflate<SearchTextItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.search_text_item,
                    parent,
                    false
                )
                ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DummyHolder -> {
                holder.binding.icon = R.drawable.ic_no_collection
                holder.binding.tip = R.string.no_recommendation
            }
            is ViewHolder -> {
                holder.binding.model = resultList[position]
                holder.binding.handler = handler
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (resultList.isNullOrEmpty()) 0 else 1

    override fun getItemCount(): Int = if (resultList.isNullOrEmpty()) 1 else resultList.size
}