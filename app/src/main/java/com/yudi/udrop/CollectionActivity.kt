package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
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
    lateinit var SQLiteManager: SQLiteManager
    private val adapter by lazy {
        CollectionAdapter(SQLiteManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SQLiteManager = SQLiteManager(this, "udrop.db", null, 1)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection)
        binding.loading = true
        binding.toolbarModel = ToolbarModel("收藏夹", R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        setupRecyclerView(binding.collectionRecyclerview)
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
        getData { collection ->
            adapter.collectionList = collection
            recyclerView.adapter = adapter
            binding.loading = false
        }
    }

    private fun getData(completion: (ArrayList<TextDetail>) -> Unit) {
        SQLiteManager.getInfo()?.let { userModel ->
            ServiceManager().getCollection(userModel.id) {
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
}