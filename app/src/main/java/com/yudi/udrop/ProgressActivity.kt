package com.yudi.udrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.adapter.ProgressAdapter
import com.yudi.udrop.databinding.ActivityProgressBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.model.local.ScheduleType

class ProgressActivity : AppCompatActivity(), ToolbarInterface, ProgressInterface {
    lateinit var binding: ActivityProgressBinding
    private val title: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.new_progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_progress)
        binding.toolbarModel = ToolbarModel(getString(title), R.drawable.ic_toolbar_back)
        setupRecyclerView(binding.progressRecyclerview)
    }

    override fun onLeftItemClick() {
        finish()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ProgressAdapter(
            this,
            if (title == R.string.new_progress) ScheduleType.NEW else ScheduleType.REVIEW
        )
    }

    companion object {
        const val INTENT_EXTRA_TITLE = "title"
    }
}