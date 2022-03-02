package com.yudi.udrop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.databinding.HomeScheduleItemBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.model.data.ScheduleModel

class HomeScheduleAdapter(val handleSchedule: ProgressInterface, val localManager: SQLiteManager) :
    RecyclerView.Adapter<HomeScheduleAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: HomeScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var newSchedule =
        ScheduleModel(localManager.countCompletedNewSchedule(), localManager.getNew().length())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var reviewSchedule = ScheduleModel(
        localManager.countCompletedReviewSchedule(),
        localManager.getReview().length()
    )
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
                model = newSchedule
                buttonText = holder.itemView.context.getString(R.string.start_to_learn)
                handler = handleSchedule
            }
            else -> with(holder.binding) {
                model = reviewSchedule
                buttonText = holder.itemView.context.getString(R.string.start_to_review)
                handler = handleSchedule
            }
        }
    }

    override fun getItemCount(): Int = 2
}