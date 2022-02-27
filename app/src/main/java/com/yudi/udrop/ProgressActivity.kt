package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.adapter.ProgressAdapter
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivityProgressBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ProgressModel
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.model.local.ScheduleType
import org.json.JSONArray
import org.json.JSONObject

class ProgressActivity : AppCompatActivity(), ToolbarInterface, ProgressInterface {
    lateinit var binding: ActivityProgressBinding
    lateinit var localManager: SQLiteManager
    private val adapter by lazy {
        ProgressAdapter(
            localManager,
            if (title == R.string.new_progress) ScheduleType.NEW else ScheduleType.REVIEW,
            this
        )
    }
    private val title: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.new_progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localManager = SQLiteManager(this, "udrop.db", null, 1)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_progress)
        binding.loading = true
        binding.toolbarModel = ToolbarModel(getString(title), R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        setupRecyclerView(binding.progressRecyclerview)
        getData()
    }

    override fun onLeftItemClick() {
        localManager.getInfo()?.let {
            if (title == R.string.new_progress)
                ServiceManager().setNewSchedule(it.id, localManager.getNew()) { requestStatus ->
                    if (!requestStatus) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this, R.string.warning, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            else
                ServiceManager().setReviewSchedule(
                    it.id,
                    localManager.getReview()
                ) { requestStatus ->
                    if (!requestStatus) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this, R.string.warning, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
        finish()
    }

    override fun checkDetail(title: String) {
        startActivity(Intent(this, TextDetailActivity::class.java).apply {
            putExtra(TextDetailActivity.INTENT_EXTRA_TEXT_TITLE, title)
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun getData() {
        localManager.getInfo()?.let { userModel ->
            ServiceManager().getSchedule(userModel.id) { new, review ->
                if (title == R.string.new_progress) getProgressList(new) else getProgressList(review)
                binding.loading = false
            }
        }
    }

    private fun getProgressList(list: JSONArray) {
        var progressList: ArrayList<ProgressModel> = arrayListOf()
        for (i in 0 until list.length()) {
            getProgressModel(list.getJSONObject(i)) {
                progressList.add(it)
                Handler(Looper.getMainLooper()).post {
                    adapter.progressList = progressList
                }
            }
        }
    }

    private fun getProgressModel(jsonObject: JSONObject, completion: (ProgressModel) -> Unit) {
        ServiceManager().getTextDetail(jsonObject.getString("title")) {
            it?.let {
                completion(
                    ProgressModel(
                        it.title, it.writer, it.content, jsonObject.getInt("done")
                    )
                )
            }
        }
    }

    companion object {
        const val INTENT_EXTRA_TITLE = "title"
    }
}