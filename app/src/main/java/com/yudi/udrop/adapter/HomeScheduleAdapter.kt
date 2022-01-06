package com.yudi.udrop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.UdropActivity
import com.yudi.udrop.databinding.HomeScheduleItemBinding
import com.yudi.udrop.model.data.ScheduleModel

class HomeScheduleAdapter : RecyclerView.Adapter<HomeScheduleAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: HomeScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<HomeScheduleItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.home_schedule_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO: get real data
        when (position) {
            0 -> with(holder.binding) {
                buttonText = holder.itemView.context.getString(R.string.start_to_learn)
                homeScheduleButton.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            UdropActivity::class.java
                        )
                    )
                }
                model = ScheduleModel("1", "10") // Test mock data
            }
            else -> with(holder.binding) {
                buttonText = holder.itemView.context.getString(R.string.start_to_review)
                model = ScheduleModel("2", "5") // Test mock data
            }
        }
    }

    override fun getItemCount(): Int = 2
}