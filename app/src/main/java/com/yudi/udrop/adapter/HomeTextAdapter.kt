package com.yudi.udrop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.HomeTextsItemBinding
import com.yudi.udrop.interfaces.OverviewInterface
import com.yudi.udrop.model.data.TextModel

class HomeTextAdapter(val handler: OverviewInterface) :
    RecyclerView.Adapter<HomeTextAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(val binding: HomeTextsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = DataBindingUtil.inflate<HomeTextsItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.home_texts_item,
            parent,
            false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(itemViewHolder: HomeViewHolder, position: Int) {
        // TODO: get real data
        itemViewHolder.binding.model = TextModel(
            "诗名 $position",
            "作者 $position",
            "这里是内容这里是内，这里是内容这里是内，这里是内容这里是内，这里是内容这里是内。",
            ""
        )
        itemViewHolder.binding.handler = handler
        itemViewHolder.binding.executePendingBindings()
    }

    // TODO: get real data
    override fun getItemCount(): Int = 4 // Test mock data
}