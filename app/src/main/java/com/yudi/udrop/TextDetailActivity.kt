package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivityTextDetailBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.TextModel
import com.yudi.udrop.model.data.ToolbarModel

class TextDetailActivity : AppCompatActivity(), ToolbarInterface {
    lateinit var binding: ActivityTextDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_detail)
        binding.toolbarModel = ToolbarModel("古诗详情", R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        binding.model = TextModel("", "", "", "", false)
        getData {
            binding.model = it
            binding.reciteBySentence.setOnClickListener {
                startActivity(Intent(this, UdropActivity::class.java).apply {
                    putExtra(UdropActivity.INTENT_EXTRA_TITLE, R.string.recite_by_sentence)
                })
            }
            binding.reciteWhole.setOnClickListener {
                startActivity(Intent(this, UdropActivity::class.java).apply {
                    putExtra(UdropActivity.INTENT_EXTRA_TITLE, R.string.recite_whole)
                })
            }
            binding.textDetailCollection.setOnClickListener {
                with(binding.model as TextModel) {
                    collected = !collected // TODO: update to service
                }
            }
        }
    }

    override fun onLeftItemClick() {
        finish()
    }

    private fun getData(completion: (TextModel) -> Unit) {
        intent?.getStringExtra(INTENT_EXTRA_TEXT_TITLE)?.let { title ->
            ServiceManager().getTextDetail(title) { detail ->
                detail?.let {
                    completion(TextModel(it.title, it.writer, it.content, it.writerInfo, false))
                }
            }
        }
    }


    companion object {
        const val INTENT_EXTRA_TEXT_TITLE = "text_title"
    }
}