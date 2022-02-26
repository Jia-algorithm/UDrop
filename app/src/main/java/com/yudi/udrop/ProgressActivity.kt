package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
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

class ProgressActivity : AppCompatActivity(), ToolbarInterface, ProgressInterface {
    lateinit var binding: ActivityProgressBinding
    lateinit var SQLiteManager: SQLiteManager
    private var newScheduleList: ArrayList<ProgressModel> = arrayListOf()
    private var reviewScheduleList: ArrayList<ProgressModel> = arrayListOf()
    private val title: Int by lazy {
        intent.getIntExtra(INTENT_EXTRA_TITLE, R.string.new_progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SQLiteManager = SQLiteManager(this, "udrop.db", null, 1)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_progress)
        binding.loading = true
        binding.toolbarModel = ToolbarModel(getString(title), R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        getData {
            if (it) setupRecyclerView(binding.progressRecyclerview) else Toast.makeText(
                this,
                R.string.warning,
                Toast.LENGTH_SHORT
            )
            binding.loading = false
        }
    }

    override fun onLeftItemClick() {
        SQLiteManager.getInfo()?.let {
            if (title == R.string.new_progress)
                ServiceManager().setNewSchedule(it.id, SQLiteManager.getNew()) { requestStatus ->
                    if (!requestStatus) Toast.makeText(this, R.string.warning, Toast.LENGTH_SHORT)
                }
            else
                ServiceManager().setReviewSchedule(
                    it.id,
                    SQLiteManager.getReview()
                ) { requestStatus ->
                    if (!requestStatus) Toast.makeText(this, R.string.warning, Toast.LENGTH_SHORT)
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
        if (title == R.string.new_progress) {
            recyclerView.adapter =
                ProgressAdapter(SQLiteManager, ScheduleType.NEW, newScheduleList, this)
            newScheduleList.map {
                SQLiteManager.addNewSchedule(it.title, it.finished)
            }
        } else {
            recyclerView.adapter =
                ProgressAdapter(SQLiteManager, ScheduleType.REVIEW, reviewScheduleList, this)
            reviewScheduleList.map {
                SQLiteManager.addReviewSchedule(it.title, it.finished)
            }
        }
    }

    private fun getData(completion: (Boolean) -> Unit) {
        SQLiteManager.getInfo()?.let {
            ServiceManager().getSchedule(it.id) { new, review ->
                newScheduleList = getProgressList(new)
                reviewScheduleList = getProgressList(review)
            }
            completion(true)
        }
        completion(false)
    }

    private fun getProgressList(list: JSONArray): ArrayList<ProgressModel> {
        var progressList: ArrayList<ProgressModel> = arrayListOf()
        for (i in 0 until list.length()) {
            ServiceManager().getTextDetail(list.getJSONObject(i).getString("title")) {
                it?.let {
                    progressList.add(
                        ProgressModel(
                            it.title,
                            it.writer,
                            it.content,
                            list.getJSONObject(i).getInt("done")
                        )
                    )
                }
            }
        }
        return progressList
    }

    companion object {
        const val INTENT_EXTRA_TITLE = "title"
    }
}