package com.yudi.udrop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.HomeScheduleItemBinding
import com.yudi.udrop.databinding.HomeTextsItemBinding
import com.yudi.udrop.model.data.ScheduleModel

class HomeScheduleAdapter : RecyclerView.Adapter<HomeScheduleAdapter.viewHolder>() {
    inner class viewHolder(val binding: HomeScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = DataBindingUtil.inflate<HomeScheduleItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.home_schedule_item,
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        //TODO:getData
        holder.binding.model = ScheduleModel("1","10")
    }

    override fun getItemCount(): Int = 2
}