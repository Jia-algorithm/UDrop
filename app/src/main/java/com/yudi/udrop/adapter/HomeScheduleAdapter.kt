package com.yudi.udrop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.UdropActivity
import com.yudi.udrop.databinding.HomeScheduleItemBinding
import com.yudi.udrop.model.data.ScheduleModel
import org.json.JSONArray

class HomeScheduleAdapter(val new_list: JSONArray, val review_list: JSONArray) :
    RecyclerView.Adapter<HomeScheduleAdapter.ViewHolder>() {
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
                model = getSchedule(new_list)
            }
            else -> with(holder.binding) {
                buttonText = holder.itemView.context.getString(R.string.start_to_review)
                homeScheduleButton.setOnClickListener {
                    onScheduleClick(it, R.string.daily_review)
                }
                model = getSchedule(review_list)
            }
        }
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

    private fun getSchedule(list: JSONArray): ScheduleModel {
        var count = 0
        for (i in 0 until list.length()) {
            if (list.getJSONObject(i).getInt("done") == 1) count++
        }
        return ScheduleModel(count, list.length())
    }

    override fun getItemCount(): Int = 2
}