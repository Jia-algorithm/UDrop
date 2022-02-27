package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yudi.udrop.adapter.SearchAdapter
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivitySearchBinding
import com.yudi.udrop.interfaces.InputInterface
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.model.local.TextDetail

class SearchActivity : AppCompatActivity(), ToolbarInterface, ProgressInterface, InputInterface {
    lateinit var binding: ActivitySearchBinding
    lateinit var localManager: SQLiteManager
    private val adapter by lazy {
        SearchAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localManager = SQLiteManager(this, "udrop.db", null, 1)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.toolbarModel = ToolbarModel("更多古诗", R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        setupRecyclerView(binding.searchRecyclerview)
        setupInputListener()
    }

    override fun checkDetail(title: String) {
        startActivity(Intent(this, TextDetailActivity::class.java).apply {
            putExtra(TextDetailActivity.INTENT_EXTRA_TEXT_TITLE, title)
        })
    }

    override fun addNewSchedule(title: String) {
        val resultCode = localManager.addNewSchedule(title, 0)
        if (resultCode == -1L)
            Snackbar.make(binding.root, R.string.already_in_schedule, Snackbar.LENGTH_SHORT).show()
        else
            Snackbar.make(binding.root, R.string.add_schedule_successfully, Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun onLeftItemClick() {
        localManager.getInfo()?.let {
            ServiceManager().setNewSchedule(it.id, localManager.getNew()) { requestStatus ->
                if (!requestStatus) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this, R.string.fail_to_update_schedule, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            finish()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN && currentFocus != null) {
            if (shouldHideInput(currentFocus!!, ev))
                hideKeyboard(this, currentFocus!!)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setupInputListener() {
        binding.searchBoxInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                getSearchData(s.toString()) { result ->
                    Handler(Looper.getMainLooper()).post {
                        adapter.resultList = result
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        binding.loading = true
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        getInitialData { result ->
            Handler(Looper.getMainLooper()).post {
                adapter.resultList = result
                binding.loading = false
            }
        }
    }

    private fun getInitialData(completion: (ArrayList<TextDetail>) -> Unit) {
        ServiceManager().getRecommendation(5) {
            val list = arrayListOf<TextDetail>()
            for (i in 0 until it.length()) {
                with(it.getJSONObject(i)) {
                    list.add(
                        TextDetail(
                            getString("title"),
                            getString("author"),
                            getString("content"),
                            ""
                        )
                    )
                }
            }
            completion(list)
        }
    }

    private fun getSearchData(
        key: String,
        completion: (ArrayList<TextDetail>) -> Unit
    ) {
        ServiceManager().searchText(key) {
            val list = arrayListOf<TextDetail>()
            for (i in 0 until it.length()) {
                with(it.getJSONObject(i)) {
                    list.add(
                        TextDetail(
                            getString("title"),
                            getString("author"),
                            getString("content"),
                            ""
                        )
                    )
                }
            }
            completion(list)
        }
    }
}