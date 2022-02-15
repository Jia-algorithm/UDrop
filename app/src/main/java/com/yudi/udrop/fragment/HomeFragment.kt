package com.yudi.udrop.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.yudi.udrop.R
import com.yudi.udrop.TextDetailActivity
import com.yudi.udrop.adapter.HomeScheduleAdapter
import com.yudi.udrop.adapter.HomeTextAdapter
import com.yudi.udrop.databinding.FragmentHomeBinding
import com.yudi.udrop.interfaces.OverviewInterface
import com.yudi.udrop.interfaces.TabLayoutInterface
import com.yudi.udrop.model.data.TextModel

class HomeFragment : Fragment(), OverviewInterface, TabLayoutInterface {
    lateinit var binding: FragmentHomeBinding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewpager()
        setupTabLayout(view)
    }

    override fun clickTextItem(model: TextModel) {
        activity?.let {
            startActivity(Intent(context, TextDetailActivity::class.java).apply {
                putExtra(TextDetailActivity.INTENT_EXTRA_TEXT_TITLE, model.Title)
            })
        }
    }

    private fun setupRecyclerView() {
        val adapter = HomeTextAdapter(this)
        val recyclerView = binding.homeTextLearned
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerView.layoutManager = layoutManager
    }

    private fun setupViewpager() {
        binding.homeScheduleViewpager.adapter = HomeScheduleAdapter()
    }

    private fun setupTabLayout(view: View) {
        val tabLayout = binding.homeScheduleTab
        val viewPager = binding.homeScheduleViewpager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView =
                LayoutInflater.from(context)
                    .inflate(R.layout.home_schedule_tab_item, null, false)
            when (position) {
                0 -> tabView.findViewById<TextView>(R.id.home_schedule_tab_item_title).text =
                    getString(R.string.new_to_learn)
                1 -> tabView.findViewById<TextView>(R.id.home_schedule_tab_item_title).text =
                    getString(R.string.to_review)
            }
            tab.customView = tabView
        }.attach()
        super.setupTabLayout(view.context, tabLayout, R.id.home_schedule_tab_item_title)
    }
}