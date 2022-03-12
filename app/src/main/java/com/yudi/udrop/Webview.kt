package com.yudi.udrop

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.yudi.udrop.databinding.WebviewBinding
import com.yudi.udrop.interfaces.ToolbarInterface
import com.yudi.udrop.model.data.ToolbarModel

class Webview : WakeupActivity(), ToolbarInterface {
    lateinit var binding: WebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.webview)
        binding.toolbarModel = ToolbarModel("新手教程", R.drawable.ic_toolbar_back)
        binding.toolbarHandler = this
        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl("file:///android_asset/help.html")
    }

    override fun onLeftItemClick() {
        onBackPressed()
    }
}