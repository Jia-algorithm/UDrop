package com.yudi.udrop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yudi.udrop.data.SQLiteManager
import com.yudi.udrop.data.ServiceManager
import com.yudi.udrop.databinding.ActivityTextDetailBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.TextModel
import com.yudi.udrop.model.data.ToolbarModel
import com.yudi.udrop.model.local.FunctionType

class TextDetailActivity : AppCompatActivity(), ToolbarInterface {
    lateinit var binding: ActivityTextDetailBinding
    lateinit var localManager: SQLiteManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localManager = SQLiteManager(this, "udrop.db", null, 1)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_detail)
        binding.toolbarModel = ToolbarModel("古诗详情", R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        binding.model = TextModel("", "", "", "")
        getData {
            Handler(Looper.getMainLooper()).post {
                binding.model = it
            }
            checkCollected(it.Title)
        }
        binding.reciteBySentence.setOnClickListener {
            startActivity(Intent(this, UdropActivity::class.java).apply {
                putExtra(UdropActivity.INTENT_EXTRA_TITLE, (binding.model as TextModel).Title)
                putExtra(UdropActivity.INTENT_EXTRA_TYPE, FunctionType.RECITE_BY_SENTENCE.typeId)
            })
        }
        binding.reciteWhole.setOnClickListener {
            startActivity(Intent(this, UdropActivity::class.java).apply {
                putExtra(UdropActivity.INTENT_EXTRA_TITLE, (binding.model as TextModel).Title)
                putExtra(UdropActivity.INTENT_EXTRA_TYPE, FunctionType.RECITE_WHOLE.typeId)
            })
        }
        binding.textDetailCollection.setOnClickListener {
            with(binding.model as TextModel) {
                localManager.getInfo()?.let { userInfo ->
                    if (collected)
                        ServiceManager().removeCollection(userInfo.id, Title) { success ->
                            if (success) collected = false
                        }
                    else
                        ServiceManager().addCollection(userInfo.id, Title) { success ->
                            if (success) collected = true
                        }
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
                    completion(
                        TextModel(
                            it.title,
                            it._writer,
                            it.content,
                            it.writerInfo
                        )
                    )
                }
            }
        }
    }

    private fun checkCollected(title: String) {
        localManager.getInfo()?.let {
            ServiceManager().checkCollection(it.id, title) { collected ->
                Handler(Looper.getMainLooper()).post {
                    binding.model?.collected = collected
                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRA_TEXT_TITLE = "text_title"
    }
}