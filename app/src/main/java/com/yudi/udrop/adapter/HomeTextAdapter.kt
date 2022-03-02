package com.yudi.udrop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.databinding.HomeTextsItemBinding
import com.yudi.udrop.databinding.NoDataItemBinding
import com.yudi.udrop.interfaces.OverviewInterface
import com.yudi.udrop.model.local.TextDetail

class HomeTextAdapter(val handler: OverviewInterface, val localManager: SQLiteManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class HomeViewHolder(val binding: HomeTextsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DummyHolder(val binding: NoDataItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var recommendList: ArrayList<TextDetail> = localManager.getRandom()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = DataBindingUtil.inflate<HomeTextsItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.home_texts_item,
                    parent,
                    false
                )
                HomeViewHolder(binding)
            }
            1 -> {
                val binding = DataBindingUtil.inflate<NoDataItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.no_data_item,
                    parent,
                    false
                )
                DummyHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(itemViewHolder: RecyclerView.ViewHolder, position: Int) {
        when (itemViewHolder) {
            is HomeViewHolder -> {
                itemViewHolder.binding.model = recommendList[position]
                itemViewHolder.binding.handler = handler
            }
            is DummyHolder -> {
                itemViewHolder.binding.tip = R.string.no_recommendation
                itemViewHolder.binding.icon = R.drawable.ic_no_collection
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (recommendList.isNullOrEmpty()) 1 else 0

    override fun getItemCount(): Int = if (recommendList.isNullOrEmpty()) 1 else recommendList.size
}