package com.yudi.udrop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.HomeTextsItemBinding
import com.yudi.udrop.model.data.TextReviewModel

class HomeAdapter (val context: Context) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(val binding: HomeTextsItemBinding) : RecyclerView.ViewHolder(binding.root)
    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = DataBindingUtil.inflate<HomeTextsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.home_texts_item,
            parent,
            false
        )
        return HomeViewHolder(binding)
    }
    //数据绑定
    override fun onBindViewHolder(itemViewHolder: HomeViewHolder, position: Int) {
        itemViewHolder.binding.model = TextReviewModel(
            "Title $position",
            "Writer $position",
            "")
        itemViewHolder.binding.executePendingBindings()
    }
    //获取item个数
    override fun getItemCount(): Int = 4//设有40个item
}