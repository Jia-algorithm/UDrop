package com.yudi.udrop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudi.udrop.R
import com.yudi.udrop.adapter.MessageAdapter
import com.yudi.udrop.databinding.FragmentUdropBinding
import com.yudi.udrop.model.data.ToolbarModel

class UdropFragment : Fragment() {
    lateinit var binding: FragmentUdropBinding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUdropBinding.inflate(inflater)
        binding.model = ToolbarModel("语滴助手")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
    }

    fun setupRecyclerView(view: View) {
        val adapter = MessageAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_udrop_content)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
    }
}