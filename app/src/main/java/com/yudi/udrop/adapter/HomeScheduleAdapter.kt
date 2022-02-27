package com.yudi.udrop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.databinding.HomeScheduleItemBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.model.data.ScheduleModel
import org.json.JSONArray

class HomeScheduleAdapter(val handleSchedule: ProgressInterface) :
    RecyclerView.Adapter<HomeScheduleAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: HomeScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var newList = JSONArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var reviewList = JSONArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        when (position) {
            0 -> with(holder.binding) {
                model = getSchedule(newList)
                buttonText = holder.itemView.context.getString(R.string.start_to_learn)
                handler = handleSchedule
            }
            else -> with(holder.binding) {
                model = getSchedule(reviewList)
                buttonText = holder.itemView.context.getString(R.string.start_to_review)
                handler = handleSchedule
            }
        }
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