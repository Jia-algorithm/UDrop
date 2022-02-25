package com.yudi.udrop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.ProgressActivity
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
                    onScheduleClick(it, R.string.daily_learn)
                }
                homeScheduleCheckProcess.setOnClickListener {
                    onCheckClick(it, R.string.new_progress)
                }
                model = ScheduleModel("1", "10") // Test mock data
            }
            else -> with(holder.binding) {
                buttonText = holder.itemView.context.getString(R.string.start_to_review)
                homeScheduleButton.setOnClickListener {
                    onScheduleClick(it, R.string.daily_review)
                }
                homeScheduleCheckProcess.setOnClickListener {
                    onCheckClick(it, R.string.review_progress)
                }
                model = ScheduleModel("2", "5") // Test mock data
            }
        }
    }

    private fun onCheckClick(view: View, title: Int) {
        view.context.startActivity(
            Intent(
                view.context,
                ProgressActivity::class.java
            ).apply {
                putExtra(ProgressActivity.INTENT_EXTRA_TITLE, title)
            }
        )
    }

    private fun onScheduleClick(view: View, title: Int) {
        view.context.startActivity(
            Intent(
                view.context,
                UdropActivity::class.java
            ).apply {
                putExtra(UdropActivity.INTENT_EXTRA_TITLE, title)
            }
        )
    }

    override fun getItemCount(): Int = 2
}