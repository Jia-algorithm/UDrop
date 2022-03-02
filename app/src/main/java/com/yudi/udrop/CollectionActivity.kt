package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.adapter.CollectionAdapter
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivityCollectionBinding
import com.yudi.udrop.interfaces.ProgressInterface
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.model.local.TextDetail

class CollectionActivity : AppCompatActivity(), ToolbarInterface, ProgressInterface {
    lateinit var binding: ActivityCollectionBinding
    lateinit var localManager: SQLiteManager
    private val adapter by lazy {
        CollectionAdapter(localManager, mainHandler, this)
    }
    private val mainHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localManager = SQLiteManager(this, "udrop.db", null, 1)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection)
        binding.loading = true
        binding.toolbarModel = ToolbarModel("收藏夹", R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        setupRecyclerView(binding.collectionRecyclerview)
    }

    override fun onResume() {
        super.onResume()
        getData { collection ->
            mainHandler.post {
                adapter.collectionList = collection
                binding.loading = false
            }
        }
    }

    override fun onLeftItemClick() {
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

    private fun getData(completion: (ArrayList<TextDetail>) -> Unit) {
        localManager.getInfo()?.let { userModel ->
            ServiceManager().getCollection(userModel.id) {
                val list = arrayListOf<TextDetail>()
                for (i in 0 until it.length()) {
                    with(it.getJSONObject(i)) {
                        list.add(
                            TextDetail(
                                getString("title"),
                                getString("author"),
                                getString("dynasty"),
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
}