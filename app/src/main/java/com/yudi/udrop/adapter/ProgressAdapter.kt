package com.yudi.udrop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.databinding.ProgressNoDataItemBinding
import com.yudi.udrop.databinding.ProgressTextItemBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.model.data.ProgressModel
import com.yudi.udrop.model.local.ScheduleType

class ProgressAdapter(
    private val localManager: SQLiteManager,
    private val scheduleType: ScheduleType,
    private val progressList: ArrayList<ProgressModel>,
    private val handler: ProgressInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val binding: ProgressTextItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DummyHolder(val binding: ProgressNoDataItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = DataBindingUtil.inflate<ProgressNoDataItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.progress_no_data_item,
                    parent,
                    false
                )
                DummyHolder(binding)
            }
            1 -> {
                val binding = DataBindingUtil.inflate<ProgressTextItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.progress_text_item,
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
            is ViewHolder -> {
                holder.binding.model = progressList[position]
                holder.binding.handler = handler
                holder.binding.removeFromSchedule.setOnClickListener {
                    when (scheduleType) {
                        ScheduleType.REVIEW -> localManager.deleteReview(progressList[position].title)
                        ScheduleType.NEW -> localManager.deleteNew(progressList[position].title)
                    }
                    progressList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                }
            }
            is DummyHolder -> {
                holder.binding.tip = when (scheduleType) {
                    ScheduleType.NEW -> R.string.no_new_schedule
                    ScheduleType.REVIEW -> R.string.no_review_schedule
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (progressList.isNullOrEmpty()) 0 else 1
    }

    override fun getItemCount(): Int = if (progressList.isNullOrEmpty()) 1 else progressList.size
}